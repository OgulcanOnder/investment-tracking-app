package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoResetPasswordRequest;
import com.ogulcanonder.investment_tracking_app.entity.PasswordResetToken;
import com.ogulcanonder.investment_tracking_app.event.PasswordChangeEvent;
import com.ogulcanonder.investment_tracking_app.exception.InvalidPasswordResetTokenException;
import com.ogulcanonder.investment_tracking_app.exception.PasswordResetTokenExpiredException;
import com.ogulcanonder.investment_tracking_app.repository.PasswordResetTokenRepository;
import com.ogulcanonder.investment_tracking_app.service.PasswordResetTokenService;
import com.ogulcanonder.investment_tracking_app.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
@Transactional(readOnly = true)
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    private final UserService userService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender javaMailSender;
    private final ApplicationEventPublisher applicationEventPublisher;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
    private static final int RESET_TOKEN_LENGTH = 32;
    private static final int RESET_TOKEN_VALIDITY_MINUTES = 15;
    private static final String PASSWORD_RESET_URL = "http://localhost:3000/reset-password?token=";

    public PasswordResetTokenServiceImpl(UserService userService,
                                         PasswordResetTokenRepository passwordResetTokenRepository,
                                         JavaMailSender javaMailSender,
                                         ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.javaMailSender = javaMailSender;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public String makeResetToken() {
        byte[] randomBytes = new byte[RESET_TOKEN_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return encoder.encodeToString(randomBytes);
    }

    @Override
    @Transactional
    public void processRequest(String email) {
        userService.findByEmail(email).ifPresent(user -> {
            if (ObjectUtils.isEmpty(user)) {
                return;
            }
            String token = makeResetToken();
            PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                    .token(token)
                    .user(user)
                    .expiresAt(LocalDateTime.now().plusMinutes(RESET_TOKEN_VALIDITY_MINUTES))
                    .build();
            passwordResetTokenRepository.save(passwordResetToken);
            sendEmail(user.getEmail(), token);
        });
    }

    private void sendEmail(String email, String token) {
        String resetUrl = PASSWORD_RESET_URL + token;
        String body = "Click the link below to reset your password \n" + resetUrl +
                "\n The link is valid for 15 minutes.\n";
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Password Reset Token");
        mailMessage.setText(body);
        javaMailSender.send(mailMessage);
    }

    private Boolean isExpired(PasswordResetToken passwordResetToken) {
        return passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now());
    }

    @Override
    @Transactional
    public void resetPassword(DtoResetPasswordRequest dtoResetPasswordRequest) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(dtoResetPasswordRequest.token())
                .orElseThrow(() -> new InvalidPasswordResetTokenException("Invalid password reset token"));
        if (isExpired(token)) {
            throw new PasswordResetTokenExpiredException("Token is expired");
        }
        userService.resetPassword(token.getUser(), dtoResetPasswordRequest.newPassword());
        passwordResetTokenRepository.delete(token);
        applicationEventPublisher.publishEvent(new PasswordChangeEvent(token.getUser().getEmail()));
    }

}
