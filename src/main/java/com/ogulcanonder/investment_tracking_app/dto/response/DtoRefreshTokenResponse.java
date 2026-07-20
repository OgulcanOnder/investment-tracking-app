package com.ogulcanonder.investment_tracking_app.dto.response;

public record DtoRefreshTokenResponse(
        String newAccessToken,
        String newRefreshToken
) {
}
