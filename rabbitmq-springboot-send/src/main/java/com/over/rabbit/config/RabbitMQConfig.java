package com.over.rabbit.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Configuration
public class RabbitMQConfig {

    //配置一个Direct类型的交换
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("bootDirectExchange");
    }
    //配置一个队列
    @Bean
    public Queue directQueue(){
        return new Queue("bootDirectQueue");
    }

    /**
     *  配置一个队列和交换机的绑定
     * @param directQueue  需要绑定的队列的对象，参数名必须要与某个@Bean的方法名完全相同这样就会自动进行注入
     * @param directExchange  需要绑定的交换机的对象，参数名必须要与某个@Bean的方法名完全相同这样就会自动进行注入
     * @return
     */
    @Bean
    public Binding directBinding(Queue directQueue,DirectExchange directExchange){
        //完成绑定
        // 参数 1 为需要绑定的队列
        // 参数 2 为需要绑定的交换机
        // 参数 3绑定时的RoutingKey
        return BindingBuilder.bind(directQueue).to(directExchange).with("bootDirectRoutingKey");
    }

        //配置一个 Fanout类型的交换
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("fanoutExchange");
    }

    //确认
    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }

//    /**
//     *生产者回调函数已经配置完毕，上面我们配置了两个回调函数setConfirmCallback和setReturnCallback
//     *
//     * 一般会产生四种情况：
//     *
//     * ①消息推送到server，但是在server里找不到交换机
//     * ②消息推送到server，找到交换机了，但是没找到队列
//     * ③消息推送到sever，交换机和队列啥都没找到
//     * ④消息推送成功
//     */
//    @Bean
//    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory){
//        RabbitTemplate rabbitTemplate = new RabbitTemplate();
//        rabbitTemplate.setConnectionFactory(connectionFactory);
//        // 设置开启Mandatory，才能触发回调函数，无论消息推送结果怎么样都强制调用回调函数
//        rabbitTemplate.setMandatory(true);
//        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
//            @Override
//            public void confirm(CorrelationData correlationData, boolean b, String s) {
//                System.out.println("ConfirmCallback    ：相关数据：" + correlationData);
//                System.out.println("ConfirmCallback    ：确认情况：" + b);
//                System.out.println("ConfirmCallback    ：原因：" + s);
//            }
//        });
//
//        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
//            @Override
//            public void returnedMessage(Message message, int i, String s, String s1, String s2) {
//                System.out.println("ReturnCallback    ：消息：" + message);
//                System.out.println("ReturnCallback    ：回应码：" + i);
//                System.out.println("ReturnCallback    ：回应消息：" + s);
//                System.out.println("ReturnCallback    ：交换机：" + s1);
//                System.out.println("ReturnCallback    ：路由键：" + s2);
//            }
//        });
//        return rabbitTemplate;
//    }
}
