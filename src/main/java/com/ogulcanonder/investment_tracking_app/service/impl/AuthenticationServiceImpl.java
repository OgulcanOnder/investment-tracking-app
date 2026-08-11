package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.dto.request.DtoLoginRequest;
import com.ogulcanonder.investment_tracking_app.dto.request.DtoRegisterUserRequest;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoAuthLoginResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoRefreshTokenResponse;
import com.ogulcanonder.investment_tracking_app.dto.response.DtoUserResponse;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.roles.Role;
import com.ogulcanonder.investment_tracking_app.service.AuthenticationService;
import com.ogulcanonder.investment_tracking_app.service.JwtService;
import com.ogulcanonder.investment_tracking_app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private static final int AUTH_HEADER_SIZE = 7;

    public AuthenticationServiceImpl(UserService userService, PasswordEncoder passwordEncoder,
                                     JwtService jwtService, AuthenticationManager authenticationManager,
                                     UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    @Override
    public DtoUserResponse register(DtoRegisterUserRequest dtoRegisterUserRequest) {
        User user = User.builder()
                .name(dtoRegisterUserRequest.name())
                .surname(dtoRegisterUserRequest.surname())
                .username(dtoRegisterUserRequest.username())
                .email(dtoRegisterUserRequest.email())
                .password(passwordEncoder.encode(dtoRegisterUserRequest.password()))
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();
        userService.create(user);
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
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String accessToken = jwtService.generateAccessToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);
            return new DtoAuthLoginResponse(accessToken, refreshToken);
        }
        logger.error("Invalid email or password");
        throw new BadCredentialsException("Invalid email or password");
    }

    @Override
    @Transactional
    public DtoRefreshTokenResponse refreshToken(String refreshToken) {
        String token = refreshToken.substring(AUTH_HEADER_SIZE);
        String email = jwtService.extractAllClaims(token).getSubject();
        String storedToken = jwtService.getRefreshToken(email);
        if (!Objects.equals(storedToken, token)) {
            throw new BadCredentialsException("Invalid refresh token");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String newAccessToken = jwtService.generateAccessToken(userDetails);
        String newRefreshToken = jwtService.generateRefreshToken(userDetails);
        return new DtoRefreshTokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public String logout() {
        User user = userService.getCurrentUser();
        jwtService.deleteRefreshToken(user.getEmail());
        return "Logged out";
    }

}
