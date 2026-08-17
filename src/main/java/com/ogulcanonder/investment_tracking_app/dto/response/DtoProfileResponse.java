package com.ogulcanonder.investment_tracking_app.dto.response;

public record DtoProfileResponse(
        String name,
        String surname,
        String username,
        String email
) {
}
