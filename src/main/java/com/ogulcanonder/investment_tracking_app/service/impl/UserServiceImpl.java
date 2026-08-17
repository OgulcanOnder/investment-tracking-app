package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoUpdatePasswordRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoProfileResponse;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.event.PasswordChangeEvent;
import com.ogulcanonder.investment_tracking_app.exception.PasswordMismatchException;
import com.ogulcanonder.investment_tracking_app.exception.ResourceNotFoundException;
import com.ogulcanonder.investment_tracking_app.mapper.UserMapper;
import com.ogulcanonder.investment_tracking_app.repository.UserRepository;
import com.ogulcanonder.investment_tracking_app.service.CurrentUserProvider;
import com.ogulcanonder.investment_tracking_app.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserProvider currentUserProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,
                           CurrentUserProvider currentUserProvider,
                           ApplicationEventPublisher applicationEventPublisher, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.currentUserProvider = currentUserProvider;
        this.applicationEventPublisher = applicationEventPublisher;
        this.userMapper = userMapper;
    }


    @Override
    public void create(User user) {
        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User already exists");
        }
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
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        if (!passwordEncoder.matches(dtoUpdatePasswordRequest.oldPassword(), user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        if (!updatePasswordMatch(dtoUpdatePasswordRequest.newPassword(),
                dtoUpdatePasswordRequest.confirmNewPassword())) {
            throw new PasswordMismatchException("Confirm new password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(dtoUpdatePasswordRequest.newPassword()));
        userRepository.updatePasswordByEmail(user.getEmail(), user.getPassword());
        applicationEventPublisher.publishEvent(new PasswordChangeEvent(email));
    }


    public boolean updatePasswordMatch(String newPassword, String confirmNewPassword) {
        return newPassword.equals(confirmNewPassword);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public User getCurrentUser() {
        return userRepository.getReferenceById(currentUserProvider.getCurrentUserId());
    }

    @Override
    public DtoProfileResponse getUserProfile() {
        return userMapper.toDto(getCurrentUser());
    }
}
