package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Java → Python
 * POST /api/v1/brainstorming/purpose 요청
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurposeRequest {
    @JsonProperty("session_id")
    private String sessionId;
    
    private String purpose;
}
