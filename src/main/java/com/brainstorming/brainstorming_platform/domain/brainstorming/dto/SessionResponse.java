package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Python → Java
 * POST /api/v1/brainstorming/session 응답
 */
@Data
public class SessionResponse {
    @JsonProperty("session_id")
    private String sessionId;
    
    private String message;
}
