package com.brainstorming.brainstorming_platform.domain.user.dto;

import com.brainstorming.brainstorming_platform.domain.user.entity.LoginProvider;
import com.brainstorming.brainstorming_platform.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {

    //클라이언트에게 보내는 필드 적용
    private Long userId;
    private String email;
    private String username;
    private LoginProvider provider;
    private LocalDateTime createdAt;

    //DTO -> Entity 변환
    public static UserResponseDto from(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getEmail(),
                user.getUsername(),
                user.getProvider(),
                user.getCreatedAt()
        );
    }
}
