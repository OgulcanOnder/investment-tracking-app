package com.ogulcanonder.investment_tracking_app.listener;

import com.ogulcanonder.investment_tracking_app.event.PasswordChangeEvent;
import com.ogulcanonder.investment_tracking_app.service.JwtService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class JwtTokenRevokeListener {
    private final JwtService jwtService;

    public JwtTokenRevokeListener(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Async
    @EventListener
    @Transactional
    public void passwordChange(PasswordChangeEvent passwordChangeEvent) {
        jwtService.revokeRefreshToken(passwordChangeEvent.email());
    }
}
