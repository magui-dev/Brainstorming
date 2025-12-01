package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import lombok.Data;

/**
 * Python → Java
 * POST /api/v1/brainstorming/confirm/{session_id} 응답
 */
@Data
public class ConfirmResponse {
    private String message;
}
