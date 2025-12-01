package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Java → Python
 * POST /api/v1/brainstorming/associations/{session_id} 요청
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssociationsRequest {
    @JsonProperty("session_id")
    private String sessionId;
    
    private List<String> associations;    // 자유연상 키워드
}
