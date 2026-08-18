package com.ogulcanonder.investment_tracking_app.service.impl;

import com.ogulcanonder.investment_tracking_app.entity.RefreshToken;
import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.exception.ResourceNotFoundException;
import com.ogulcanonder.investment_tracking_app.repository.RefreshTokenRepository;
import com.ogulcanonder.investment_tracking_app.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.key}")
    private String JWT_KEY;

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 900000L;
    private static final long REFRESH_TOKEN_VALIDITY_SECONDS = 604800000L;

    private final RefreshTokenRepository refreshTokenRepository;

    public JwtServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public String generateAccessToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", ((User) userDetails).getId());
        claims.put("username", ((User) userDetails).getRealUsername());
        claims.put("roles", userDetails.getAuthorities());
        return createToken(claims, userDetails.getUsername(), ACCESS_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @Override
    public Boolean validateToken(String token, UserDetails userDetails) {
        String email = extractAllClaims(token).getSubject();
        Date expiration = extractAllClaims(token).getExpiration();
        return userDetails.getUsername().equals(email) && expiration.after(new Date());
    }

    @Override
    public SecretKey getSecretKey() {
        byte[] secretKey = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(secretKey);
    }

    @Override
    public String createToken(Map<String, Object> claims, String email, long expireTime) {
        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSecretKey(), Jwts.SIG.HS256)
                .compact();
    }

    @Override
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", ((User) userDetails).getId());
        claims.put("roles", userDetails.getAuthorities());
        String refreshToken = createToken(claims, userDetails.getUsername(), REFRESH_TOKEN_VALIDITY_SECONDS);
        saveRefreshToken(userDetails.getUsername(), refreshToken, REFRESH_TOKEN_VALIDITY_SECONDS);
        return refreshToken;
    }

    @Override
    public String getRefreshToken(String email) {
        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token not found"));
        return refreshToken.getRefreshToken();
    }

    @Override
    @Transactional
    public void saveRefreshToken(String email, String refreshToken, long expireTime) {
        RefreshToken saveRefreshToken = refreshTokenRepository.findByEmail(email)
                .orElseGet(RefreshToken::new);
        saveRefreshToken.setEmail(email);
        saveRefreshToken.setRefreshToken(refreshToken);
        LocalDateTime expiresAt = LocalDateTime.now().plus(Duration.ofMillis(expireTime));
        saveRefreshToken.setExpirationTime(expiresAt);
        refreshTokenRepository.save(saveRefreshToken);
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String email) {
        refreshTokenRepository.deleteRefreshTokenByEmail(email);
    }

    @Override
    public void revokeRefreshToken(String email) {
        refreshTokenRepository.deleteRefreshTokenByEmail(email);
    }

    @Override
    @Transactional
    public void deleteExpiredRefreshTokens() {
        refreshTokenRepository.deleteAllByExpirationTimeBefore(LocalDateTime.now());
    }
}
