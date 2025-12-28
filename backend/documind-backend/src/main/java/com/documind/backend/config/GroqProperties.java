package com.documind.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Groq API 설정 프로퍼티
 * application.yml의 groq.api 설정을 바인딩
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "groq.api")
public class GroqProperties {
    private String key;        // API 키
    private String baseUrl;    // API 기본 URL
    private String model;      // 사용할 모델명
    private int timeout;       // 타임아웃 (ms)
}