package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoUpdatePasswordRequest;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.repository.UserRepository;
import com.ogulcanonder.investment_tracking_app.service.JwtService;
import com.ogulcanonder.investment_tracking_app.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void resetPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updatePassword(String email, DtoUpdatePasswordRequest dtoUpdatePasswordRequest) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(dtoUpdatePasswordRequest.oldPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        if (!updatePasswordMatch(dtoUpdatePasswordRequest.newPassword(),
                dtoUpdatePasswordRequest.confirmNewPassword())) {
            throw new RuntimeException("Confirm new password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dtoUpdatePasswordRequest.newPassword()));
        userRepository.updatePasswordByEmail(user.getEmail(), user.getPassword());
        jwtService.revokeRefreshToken(user.getEmail());
    }

    public boolean updatePasswordMatch(String newPassword, String confirmNewPassword) {
        return newPassword.equals(confirmNewPassword);
    }
}
