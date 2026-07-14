package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.config.PasswordEncoderConfig;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoRegisterUserRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoUserResponse;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.repository.UserRepository;
import com.ogulcanonder.investment_tracking_app.roles.Role;
import com.ogulcanonder.investment_tracking_app.service.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoderConfig passwordEncoderConfig) {
        this.userRepository = userRepository;
        this.passwordEncoderConfig = passwordEncoderConfig;
    }

    @Override
    public DtoUserResponse register(DtoRegisterUserRequest dtoRegisterUserRequest) {
        User user = User.builder()
                .name(dtoRegisterUserRequest.name())
                .surname(dtoRegisterUserRequest.surname())
                .username(dtoRegisterUserRequest.username())
                .email(dtoRegisterUserRequest.email())
                .password(passwordEncoderConfig.passwordEncoder().encode(dtoRegisterUserRequest.password()))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();
        userRepository.save(user);
        return new DtoUserResponse(user.getName(), user.getSurname(), user.getUsername(), user.getEmail(),
                user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled(),
                user.getAuthorities());
    }


}
