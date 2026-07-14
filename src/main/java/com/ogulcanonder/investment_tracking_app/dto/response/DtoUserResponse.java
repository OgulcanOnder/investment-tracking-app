package com.ogulcanonder.investment_tracking_app.dto.response;

import com.ogulcanonder.investment_tracking_app.roles.Role;

import java.util.Set;

public record DtoUserResponse(
        String name,
        String surname,
        String username,
        String email,
        boolean isAccountNonExpired,
        boolean isAccountNonLocked,
        boolean isCredentialsNonExpired,
        boolean isEnabled,
        Set<Role> authorities

) {
}
