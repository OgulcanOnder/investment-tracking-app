package com.ogulcanonder.investment_tracking_app.repository;

import com.ogulcanonder.investment_tracking_app.entity.User;
import com.ogulcanonder.investment_tracking_app.roles.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.TestConstructor;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class UserRepositoryTest {

    private final UserRepository userRepository;
    private final TestEntityManager testEntityManager;

    public UserRepositoryTest(UserRepository userRepository, TestEntityManager testEntityManager) {
        this.userRepository = userRepository;
        this.testEntityManager = testEntityManager;
    }

    @DisplayName("should update password with user email when updated password")
    @Test
    public void shouldUpdatePasswordWithUserEmailWhenUpdatedPassword(){
        String email="test email";
        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email(email)
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        testEntityManager.persistAndFlush(user);
        String newPassword = "newPassword";
        testEntityManager.clear();

        userRepository.updatePasswordByEmail(email, newPassword);
        String userPassword=testEntityManager.find(User.class,user.getId()).getPassword();
        assertThat(userPassword).isEqualTo(newPassword);
    }
    @DisplayName("should not update password with not user email when not update password")
    @Test
    public void shouldNotUpdatePasswordWithUserEmailWhenUpdatedPassword(){
        String email="test email";
        String wrongEmail="wrong email";
        User user = User.builder()
                .name("test name")
                .surname("test surname")
                .username("test username")
                .email(email)
                .password("test password")
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .authorities(Set.of(Role.ROLE_USER))
                .build();

        testEntityManager.persistAndFlush(user);
        String newPassword = "newPassword";
        testEntityManager.clear();

        userRepository.updatePasswordByEmail(wrongEmail, newPassword);
        String userPassword=testEntityManager.find(User.class,user.getId()).getPassword();
        assertThat(userPassword).isEqualTo(user.getPassword());
    }


}
