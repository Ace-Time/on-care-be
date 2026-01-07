package org.ateam.oncare.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.ateam.oncare.config.logutil.LogInterceptor;
import org.ateam.oncare.config.logutil.ServletCachingFilter;
import org.ateam.oncare.global.customannotation.resolver.ClientIpArgumentResolver;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
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
                .addPathPatterns("/api/**") // 적용할 URL 패턴
                .excludePathPatterns("/css/**", "/images/**", "/js/**"); // 제외할 패턴
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

    // 필터 등록 (Spring Security보다 앞에 오도록 설정)
    @Bean
    public FilterRegistrationBean<ServletCachingFilter> servletCachingFilter() {
        FilterRegistrationBean<ServletCachingFilter> registrationBean = new FilterRegistrationBean<>();

        // 1. 필터 객체 생성
        registrationBean.setFilter(new ServletCachingFilter());

        // 2. 모든 URL에 적용
        registrationBean.addUrlPatterns("/*");

        // 3. [추가] 비동기 요청이나 에러 요청 시에도 필터가 동작하도록 설정
        registrationBean.setDispatcherTypes(
                DispatcherType.REQUEST,
                DispatcherType.ASYNC,
                DispatcherType.ERROR
        );

        // 4. 순서 최우선 순위 (Spring Security보다 앞섬)
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return registrationBean;
    }
}
