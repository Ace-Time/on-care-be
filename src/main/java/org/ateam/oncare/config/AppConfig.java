package org.ateam.oncare.config;

import lombok.RequiredArgsConstructor;
import org.ateam.oncare.config.interceptor.LogInterceptor;
import org.ateam.oncare.global.customannotation.resolver.ClientIpArgumentResolver;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(AppConfig.class, args);
    }
    private final ClientIpArgumentResolver clientIpArgumentResolver;

    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor())
                .addPathPatterns("/**");
    }

    /**
     * ClientIP를 얻는 커스텀 어노테이션 기능 설정
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(clientIpArgumentResolver);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
