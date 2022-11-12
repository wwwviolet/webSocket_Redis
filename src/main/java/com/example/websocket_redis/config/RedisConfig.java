package com.example.websocket_redis.config;

import com.example.websocket_redis.ws.ChatEndPoint;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<Object,Object> redisTemplate (RedisConnectionFactory
                                                               redisConnectionFactory)
    {
        RedisTemplate<Object,Object> template= new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jsonRedisSerializer =
                new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om= new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.activateDefaultTyping( LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.WRAPPER_ARRAY);
        jsonRedisSerializer.setObjectMapper(om);
        template.setDefaultSerializer(jsonRedisSerializer);
        return  template;
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,MessageListenerAdapter adapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(adapter, new PatternTopic("topic"));
        return container;
    }

    @Bean
    public MessageListenerAdapter adapter(ChatEndPoint listener){
        System.out.println("到这");
        return new MessageListenerAdapter(listener,"onMessage");
    }
}
