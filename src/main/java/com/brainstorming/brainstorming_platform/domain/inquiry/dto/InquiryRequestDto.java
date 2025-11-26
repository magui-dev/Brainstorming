package com.brainstorming.brainstorming_platform.domain.inquiry.dto;

import com.brainstorming.brainstorming_platform.domain.inquiry.entity.Inquiry;
import com.brainstorming.brainstorming_platform.domain.inquiry.entity.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InquiryRequestDto {

    private Long userId; //누가작성했는지
    private String title; //문의 제목
    private String content; // 문의 내용

    public Inquiry toEntity() {
        return new Inquiry(
                null,  //InquiryId 자동생성
                userId, //userId 포함
                title,
                content,
                InquiryStatus.PENDING, //기본값
                null  //Reply는 null
        );
    }
}

