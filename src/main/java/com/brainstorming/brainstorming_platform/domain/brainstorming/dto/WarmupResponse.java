package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import lombok.Data;
import java.util.List;

/**
 * Python → Java
 * GET /api/v1/brainstorming/warmup/{session_id} 응답
 */
@Data
public class WarmupResponse {
    private List<String> questions;    // 워밍업 질문 3개
}
