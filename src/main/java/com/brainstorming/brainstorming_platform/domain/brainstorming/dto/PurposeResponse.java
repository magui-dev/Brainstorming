package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import lombok.Data;

/**
 * Python → Java
 * POST /api/v1/brainstorming/purpose 응답
 */
@Data
public class PurposeResponse {
    private String message;
    private String purpose;
}
