package com.brainstorming.brainstorming_platform.domain.user.dto;

import com.brainstorming.brainstorming_platform.domain.user.entity.LoginProvider;
import com.brainstorming.brainstorming_platform.domain.user.entity.MyRole;
import com.brainstorming.brainstorming_platform.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    //클라이언트가 보내는 필드
    private String email;
    private String username;
    private LoginProvider provider;
    private String providerId;

    //Dto -> Entity 변환
    public User toEntity() {
        return new User(
                null, //Id는 자동생성되어 null처리
                email,
                username,
                provider,
                providerId,
                MyRole.USER //기본값
        );
    }
}
