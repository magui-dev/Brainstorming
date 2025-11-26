package com.brainstorming.brainstorming_platform.domain.user.service;

import com.brainstorming.brainstorming_platform.domain.user.entity.LoginProvider;
import com.brainstorming.brainstorming_platform.domain.user.entity.MyRole;
import com.brainstorming.brainstorming_platform.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional // 테스트 후 자동 롤백
class UserServiceTest {

    @Autowired // 의존성 자동 주입
    private UserService userService;

    @Test
    @DisplayName("사용자 저장 테스트")
    void save() {
        // given 유저정보 생성
        User user = new User(
                null,
                "test@example.com",
                "테스트유저",
                LoginProvider.GOOGLE,
                "google123",
                MyRole.USER
        );

        // when 유저정보 저장
        User savedUser = userService.save(user);

        // then 생성정보 확인
        assertThat(savedUser.getUserId()).isNotNull(); // ID 자동 생성 확인
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getUsername()).isEqualTo("테스트유저");
        assertThat(savedUser.getProvider()).isEqualTo(LoginProvider.GOOGLE);
        assertThat(savedUser.getRole()).isEqualTo(MyRole.USER);
    }

    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void findById() {
        // given
        User user = new User(null, "find@test.com", "찾기테스트", 
                LoginProvider.KAKAO, "kakao456", MyRole.USER);
        User savedUser = userService.save(user);

        // when
        User foundUser = userService.findById(savedUser.getUserId());

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo("find@test.com");
        assertThat(foundUser.getUsername()).isEqualTo("찾기테스트");
    }

    @Test
    @DisplayName("존재하지 않는 ID로 조회 시 예외 발생")
    void findByIdNotFound() {
        // given
        Long nonExistentId = 999L;

        // when & then
        assertThatThrownBy(() -> userService.findById(nonExistentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이디가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("이메일로 사용자 조회 성공")
    void findByEmail() {
        // given
        User user = new User(null, "email@test.com", "이메일테스트",
                LoginProvider.NAVER, "naver789", MyRole.USER);
        userService.save(user);

        // when
        Optional<User> foundUser = userService.findByEmail("email@test.com");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getUsername()).isEqualTo("이메일테스트");
        assertThat(foundUser.get().getProvider()).isEqualTo(LoginProvider.NAVER);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회 시 빈 Optional 반환")
    void findByEmailNotFound() {
        // given
        String nonExistentEmail = "notfound@test.com";

        // when
        Optional<User> foundUser = userService.findByEmail(nonExistentEmail);

        // then
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("사용자 삭제 성공")
    void delete() {
        // given
        User user = new User(null, "delete@test.com", "삭제테스트",
                LoginProvider.GOOGLE, "google999", MyRole.USER);
        User savedUser = userService.save(user);
        Long userId = savedUser.getUserId();

        // when
        userService.delete(userId);

        // then
        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("아이디가 존재하지 않습니다.");
    }
}
