package org.ateam.oncare.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.ateam.oncare.beneficiary.command.service.NotificationSubscriberForRedis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    // Topic 정의
    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("notificationsToEmployee");
    }

    // 리스터 어댑터 설정(알람 발생시 받을 메소드 연결)
    @Bean
    public MessageListenerAdapter listenerAdapter(NotificationSubscriberForRedis subscriber) {
        return new MessageListenerAdapter(subscriber, "notificationsToEmployee");
    }

    // Redis연결 (topic,리스너 맵핑)
    @Bean
    public RedisMessageListenerContainer container(
            RedisConnectionFactory redisConnectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic channelTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(listenerAdapter, channelTopic);
        return container;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 1. Key는 String
        template.setKeySerializer(new StringRedisSerializer());

        // 2. Value를 위한 ObjectMapper 설정 (여기가 핵심!)
        ObjectMapper objectMapper = new ObjectMapper();

        // (중요) 날짜 타입(LocalDateTime) 지원 모듈 등록
        objectMapper.registerModule(new JavaTimeModule());

        // (중요) 날짜를 [2024, 1, 6...] 배열이 아니라 "2024-01-06T..." 문자열로 저장
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // (중요) 역직렬화 시 타입 정보(@class)를 저장하도록 설정
        // 이걸 해야 나중에 꺼낼 때 LinkedHashMap 오류를 줄일 수 있음
        objectMapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL
        );

        // 커스텀 ObjectMapper가 적용된 Serializer 생성
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        return template;
    }
}
