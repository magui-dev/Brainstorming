package com.brainstorming.brainstorming_platform.domain.brainstorming.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

/**
 * Python → Java
 * GET /api/v1/brainstorming/ideas/{session_id} 응답
 */
@Data
public class IdeasResponse {
    private List<IdeaDto> ideas;
    
    @JsonProperty("rag_context")
    private List<String> ragContext;    // RAG로 검색된 브레인스토밍 기법
    
    /**
     * Python에서 받은 아이디어 정보
     */
    @Data
    public static class IdeaDto {
        private String title;
        private String description;
        private String analysis;  // ✅ Python이 보내는 필드명
    }
}
