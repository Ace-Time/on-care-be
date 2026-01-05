package org.ateam.oncare.config;

import org.ateam.oncare.beneficiary.command.service.NotificationSubscriberForRedis;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

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
}
