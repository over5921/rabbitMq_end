package com.over.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 学相伴-飞哥
 * @description: RabbitMQConfiguration
 * @Date : 2021/3/6
 */
@Configuration
public class RabbitMQConfiguration {


    // 创建死信交换机
    @Bean
    public FanoutExchange deadExchange() {
        return new FanoutExchange("dead_order_fanout_exchange", true, false);
    }

    // 死信队列
    @Bean
    public Queue deadOrderQueue() {
        return new Queue("dead.order.queue", true);
    }

    // 私信交换机绑定死信队列
    @Bean
    public Binding bindDeadOrder() {
        return BindingBuilder.bind(deadOrderQueue()).to(deadExchange());
    }


    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("order_fanout_exchange", true, false);
    }

    // 队列绑定私信交换机
    @Bean
    public Queue orderQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", "dead_order_fanout_exchange");
        return new Queue("order.queue", true, false, false, args);
    }

    @Bean
    public Binding bindorder() {
        return BindingBuilder.bind(orderQueue()).to(fanoutExchange());
    }

}
