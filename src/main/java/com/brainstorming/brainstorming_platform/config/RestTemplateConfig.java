package com.brainstorming.brainstorming_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 설정
 * Python FastAPI 호출용
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(30000);   // 연결 타임아웃 30초
        factory.setReadTimeout(120000);     // 읽기 타임아웃 120초 (LLM 호출 고려)
        
        return new RestTemplate(factory);
    }
}
