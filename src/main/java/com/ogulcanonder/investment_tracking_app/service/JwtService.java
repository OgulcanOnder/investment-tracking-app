package com.ogulcanonder.investment_tracking_app.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.util.Map;

public interface JwtService {

    public String generateAccessToken(String email);

    public Claims extractAllClaims(String token);

    public Boolean validateToken(String token, UserDetails userDetails);

    public SecretKey getSecretKey();

    public String createToken(Map<String, Object> claims, String email, long expireTime);

    public String generateRefreshToken(String email);

    public String getRefreshToken(String email);

    public void saveRefreshToken(String email, String refreshToken, long expireTime);

    public void deleteRefreshToken(String token);

    public void revokeRefreshToken(String email);
}
