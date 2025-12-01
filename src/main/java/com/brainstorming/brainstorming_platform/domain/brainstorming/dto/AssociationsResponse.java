package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import lombok.Data;
import java.util.List;

/**
 * Python → Java
 * POST /api/v1/brainstorming/associations/{session_id} 응답
 */
@Data
public class AssociationsResponse {
    private String message;
    private List<String> associations;
}
