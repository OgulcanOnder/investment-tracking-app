package com.ogulcanonder.investment_tracking_app.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Map;

public interface JwtService {

    public String generateAccessToken(UserDetails userDetails);

    public Claims extractAllClaims(String token);

    public Boolean validateToken(String token, UserDetails userDetails);

    public SecretKey getSecretKey();

    public String createToken(Map<String, Object> claims, String email, long expireTime);

    public String generateRefreshToken(UserDetails userDetails);

    public String getRefreshToken(String email);

    public void saveRefreshToken(String email, String refreshToken, long expireTime);

    public void deleteRefreshToken(String token);

    public void revokeRefreshToken(String email);

    public void deleteExpiredRefreshTokens();
}
