package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.service.CurrentUserProvider;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityCurrentUserProvider implements CurrentUserProvider {
    @Override
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new AuthenticationCredentialsNotFoundException("User is not authenticated");
        }
        return user.getId();
    }

}
