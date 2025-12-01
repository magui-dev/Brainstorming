package com.brainstorming.brainstorming_platform.domain.brainstorming.controller;

import com.brainstorming.brainstorming_platform.domain.brainstorming.dto.BrainstormRequest;
import com.brainstorming.brainstorming_platform.domain.brainstorming.dto.BrainstormResponse;
import com.brainstorming.brainstorming_platform.domain.brainstorming.service.BrainstormingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 브레인스토밍 컨트롤러
 * 사용자 요청을 받아 Python 브레인스토밍 모듈 호출
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/brainstorm")
public class BrainstormController {

    private final BrainstormingService brainstormingService;

    /**
     * 브레인스토밍 아이디어 생성
     * 
     * POST /api/brainstorm/generate
     * 
     * 요청 예시:
     * {
     *   "userId": 1,
     *   "purpose": "학생들을 위한 학습 앱 아이디어",
     *   "associations": ["학습", "AI", "맞춤형", "학생", "효율"]
     * }
     * 
     * @param request 브레인스토밍 요청
     * @return 생성된 아이디어 목록
     */
    @PostMapping("/generate")
    public ResponseEntity<BrainstormResponse> generateIdeas(@RequestBody BrainstormRequest request) {
        // 입력 검증
        if (request.getUserId() == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (request.getPurpose() == null || request.getPurpose().trim().isEmpty()) {
            throw new IllegalArgumentException("purpose는 필수입니다.");
        }
        if (request.getAssociations() == null || request.getAssociations().isEmpty()) {
            throw new IllegalArgumentException("associations는 최소 1개 이상 필요합니다.");
        }

        // 브레인스토밍 실행
        BrainstormResponse response = brainstormingService.generate(request);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 상태 확인
     * GET /api/brainstorm/status
     */
    @GetMapping("/status")
    public ResponseEntity<String> checkStatus() {
        return ResponseEntity.ok("Brainstorming API is ready!");
    }
}
