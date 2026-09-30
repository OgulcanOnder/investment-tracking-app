package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.RefreshToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestConstructor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class RefreshTokenRepositoryTest {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TestEntityManager testEntityManager;

    public RefreshTokenRepositoryTest(RefreshTokenRepository refreshTokenRepository, TestEntityManager testEntityManager) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.testEntityManager = testEntityManager;
    }

    @DisplayName("should delete refresh token with email when deleted refresh token")
    @Test
    public void shouldDeleteRefreshTokenWithEmailWhenDeletedRefreshToken() {
        String email = "testemail@gmail.com";
        RefreshToken refreshToken= RefreshToken.builder()
                .email(email)
                .refreshToken("test refresh token")
                .expirationTime(LocalDateTime.now())
                .build();
        testEntityManager.persistAndFlush(refreshToken);
        refreshTokenRepository.deleteRefreshTokenByEmail(email);
        testEntityManager.clear();
        assertThat(testEntityManager.find(RefreshToken.class, refreshToken.getId())).isNull();
    }

    @DisplayName("should delete refresh token with email when deleted refresh token")
    @Test
    public void shouldDeleteRefreshTokenWithNotEmailWhenNotDeleteRefreshToken() {
        String email = "testemail@gmail.com";
        String wrongEmail = "wrongemail@gmail.com";
        RefreshToken refreshToken= RefreshToken.builder()
                .email(email)
                .refreshToken("test refresh token")
                .expirationTime(LocalDateTime.now())
                .build();
        testEntityManager.persistAndFlush(refreshToken);
        refreshTokenRepository.deleteRefreshTokenByEmail(wrongEmail);
        assertThat(testEntityManager.find(RefreshToken.class, refreshToken.getId())).isEqualTo(refreshToken);
        testEntityManager.clear();
    }

}
