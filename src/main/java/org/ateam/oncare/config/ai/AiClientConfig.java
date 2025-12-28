package org.ateam.oncare.config.ai;

// RestTemplate 설정 (타임아웃 포함)

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AiClientConfig {

    @Bean
    public RestTemplate aiRestTemplate(RestTemplateBuilder builder) {
        return builder
                .connectTimeout(Duration.ofSeconds(3))  // Boot4: setConnectTimeout 아님
                .readTimeout(Duration.ofSeconds(15))    // Boot4: setReadTimeout 아님
                .build();
    }
//    @Bean
//    public RestTemplate aiRestTemplate() {
//        var factory = new SimpleClientHttpRequestFactory();
//        factory.setConnectTimeout(3000);
//        factory.setReadTimeout(15000);
//        return new RestTemplate(factory);
//    }
}