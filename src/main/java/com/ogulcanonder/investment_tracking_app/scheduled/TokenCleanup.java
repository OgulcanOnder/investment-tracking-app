package com.ogulcanonder.investment_tracking_app.scheduled;

import com.ogulcanonder.investment_tracking_app.service.JwtService;
import com.ogulcanonder.investment_tracking_app.service.PasswordResetTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableScheduling
@Transactional(readOnly = true)
public class TokenCleanup {
    private final JwtService jwtService;
    private final PasswordResetTokenService passwordResetTokenService;

    public TokenCleanup(JwtService jwtService, PasswordResetTokenService passwordResetTokenService) {
        this.jwtService = jwtService;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Scheduled(cron = "0 35 15 * * *", zone = "Europe/Istanbul")
    @Transactional
    public void clearExpiredTokens() {
        jwtService.deleteExpiredRefreshTokens();
        passwordResetTokenService.deleteExpiredTokens();
    }
}
