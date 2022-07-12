package com.over.moodymq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TimeRabbitConfig {

    @Bean
    public Queue TimeQueue() {
        return new Queue("TimeQueue", true);
    }

    @Bean
    DirectExchange TimeExchange() {
        return new DirectExchange("TimeExchange", true, false);
    }

    @Bean
    Binding bindingTimeDlDirect() {
        return BindingBuilder.bind(TimeQueue()).to(TimeExchange()).with("timedlRouteKey");
    }

    @Bean
    public Queue TimeDirectQueue() {
        Map<String,Object> params=new HashMap<>();
        params.put("x-dead-letter-exchange", "TimeExchange");
        params.put("x-dead-letter-routing-key", "timedlRouteKey");
        params.put("x-message-ttl", 20000);//20s内没被消费即丢入死信队列
        params.put("x-max-length", 50);
        return new Queue("TimeDirectQueue", true,false,false,params);
    }

    @Bean
    DirectExchange TimeDirectExchange() {
        return new DirectExchange("TimeDirectExchange", true, false);
    }

    @Bean
    Binding bindingTimeDirect() {
        return BindingBuilder.bind(TimeDirectQueue()).to(TimeDirectExchange()).with("timeRouteKey");
    }
}
