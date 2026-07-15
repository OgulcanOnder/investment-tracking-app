package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.config.PasswordEncoderConfig;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoLoginRequest;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoRegisterUserRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoAuthLoginResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoUserResponse;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.repository.UserRepository;
import com.ogulcanonder.investment_tracking_app.roles.Role;
import com.ogulcanonder.investment_tracking_app.service.AuthenticationService;
import com.ogulcanonder.investment_tracking_app.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private static final int AUTHHEADER_SIZE = 7;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoderConfig passwordEncoderConfig,
                                     JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoderConfig = passwordEncoderConfig;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
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

    @Transactional
    @Override
    public DtoAuthLoginResponse login(DtoLoginRequest dtoLoginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                dtoLoginRequest.email(), dtoLoginRequest.password()));
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateAccessToken(dtoLoginRequest.email());
            String refreshToken = jwtService.generateRefreshToken(dtoLoginRequest.email());
            return new DtoAuthLoginResponse(accessToken, refreshToken);
        }
        logger.error("Invalid email or password");
        throw new BadCredentialsException("Invalid email or password");
    }

    public String refreshToken(String refreshToken) {
        String token = refreshToken.substring(AUTHHEADER_SIZE);
        String email = jwtService.extractAllClaims(token).getSubject();
        String storedToken = jwtService.getRefreshToken(email);
        if (storedToken.equals(token)) {
            return jwtService.generateAccessToken(email);
        }
        throw new RuntimeException("Invalid refresh token");
    }

    @Override
    @Transactional
    public String logout(String authHeader) {
        String token = authHeader.substring(AUTHHEADER_SIZE);
        jwtService.deleteRefreshToken(token);
        return "Logged out";
    }


}
