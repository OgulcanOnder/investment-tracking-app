package com.ogulcanonder.investment_tracking_app.dto.response;

public record DtoAuthLoginResponse(
        String accessToken,
        String refreshToken
) {
}
