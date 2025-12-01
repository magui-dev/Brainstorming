package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import com.brainstorming.brainstorming_platform.domain.idea.dto.IdeaResponseDto;
import lombok.Data;
import java.util.List;

/**
 * Java Controller → 사용자
 * 브레인스토밍 결과
 */
@Data
public class BrainstormResponse {
    private String sessionId;                     // 세션 ID
    private List<IdeaResponseDto> ideas;          // 생성된 아이디어 목록 (DB 저장 후)
    private String message;                       // 메시지
}
