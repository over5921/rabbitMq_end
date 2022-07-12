package com.over.moodymq.config;
 
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 延迟队列功能
 * ttl死信队列
 * 通过设置队列的ttl过期时间，当消息在队列中超过规定的ttl时间时未被消费时，便会抛到绑定的死信队列中去消费
 * @program: rabbitmq
 * @description: ttl延迟队列配置类 @Bean可以设置组件的名字 没有设置时以方法名为组件名字
 * @author: LiuZhuzheng
 * @create: 2021-12-03 16:07
 **/
@Configuration
public class TtlQueueConfig {
    /** 普通交换机*/
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    /** 死信交换机*/
    public static final String DEAD_EXCHANGE = "dead_exchange";
    /** 普通队列a*/
    public static final String NORMAL_QUEUE_A = "normal_queue_a";
    /** 普通队列b*/
    public static final String NORMAL_QUEUE_B = "normal_queue_b";
    /** 普通队列c*/
    public static final String NORMAL_QUEUE_C = "normal_queue_c";
    /** 死信队列*/
    public static final String DEAD_QUEUE = "dead_queue";
    /** 普通队列a路由key*/
    public static final String NORMAL_ROUTING_KEY_A = "normal_routing_key_a";
    /** 普通队列b路由key*/
    public static final String NORMAL_ROUTING_KEY_B = "normal_routing_key_b";
    /** 普通队列c路由key*/
    public static final String NORMAL_ROUTING_KEY_C = "normal_routing_key_c";
    /** 死信队列路由key*/
    public static final String DEAD_ROUTING_KEY = "dead_routing_key";
 
    /**
     * 声明普通交换机
     * 使用直接交换机 发布订阅方式
     * 默认持久化 消费者断开连接时不自动删除
     * */
    @Bean
    public DirectExchange normalExchange(){
        return new DirectExchange(NORMAL_EXCHANGE);
    }
 
    /**
     * 声明死信交换机
     * */
    @Bean
    public DirectExchange deadExchange(){
        return new DirectExchange(DEAD_EXCHANGE);
    }
 
    /**
     * 声明普通队列a
     * 设置ttl时间为10秒
     * */
    @Bean
    public Queue queueA(){
        // Map<String,Object> params = new HashMap<>();
        // //声明当前队列绑定的死信交换机
        // params.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        // //声明当前队列的死信路由key
        // params.put("x-dead-letter-routing-key",DEAD_ROUTING_KEY);
        // //声明队列ttl
        // params.put("x-message-ttl",10000);
        // return QueueBuilder.durable(NORMAL_QUEUE_A).withArguments(params).build();
        return QueueBuilder.durable(NORMAL_QUEUE_A)
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .ttl(10000)
                .build();
    }
 
    /**
     * 声明普通队列b
     * 设置ttl时间为40秒
     * */
    @Bean
    public Queue queueB(){
        return QueueBuilder.durable(NORMAL_QUEUE_B)
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .ttl(40000)
                .build();
    }
 
    /**
     * 声明普通队列c
     * */
    @Bean
    public Queue queueC(){
        return QueueBuilder.durable(NORMAL_QUEUE_C)
                .deadLetterExchange(DEAD_EXCHANGE)
                .deadLetterRoutingKey(DEAD_ROUTING_KEY)
                .build();
    }
 
    /**
     * 声明死信队列
     * */
    @Bean
    public Queue queueD(){
        return QueueBuilder.durable(DEAD_QUEUE).build();
    }
 
    /**
     * 声明普通队列a与普通交换机绑定 设置路由key
     * 参数因为已经注册在容器中 会自动从容器中取 但是名字必须一样
     * */
    @Bean
    public Binding queueBindingA(Queue queueA, DirectExchange normalExchange){
        return BindingBuilder.bind(queueA).to(normalExchange).with(NORMAL_ROUTING_KEY_A);
    }
 
    /**
     * 声明普通队列b与普通交换机绑定 设置路由key
     * */
    @Bean
    public Binding queueBindingB(Queue queueB, DirectExchange normalExchange){
        return BindingBuilder.bind(queueB).to(normalExchange).with(NORMAL_ROUTING_KEY_B);
    }
 
    /**
     * 声明普通队列c与普通交换机绑定 设置路由key
     * */
    @Bean
    public Binding queueBindingC(Queue queueC, DirectExchange normalExchange){
        return BindingBuilder.bind(queueC).to(normalExchange).with(NORMAL_ROUTING_KEY_C);
    }
 
    /**
     * 声明死信队列与死信交换机绑定
     * */
    @Bean
    public Binding queueBindingD(Queue queueD, DirectExchange deadExchange){
        return BindingBuilder.bind(queueD).to(deadExchange).with(DEAD_ROUTING_KEY);
    }
}