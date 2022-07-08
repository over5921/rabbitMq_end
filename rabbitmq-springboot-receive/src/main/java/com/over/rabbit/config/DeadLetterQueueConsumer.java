package com.over.rabbit.config;
 
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
 
/**
 * @program: rabbitmq
 * @description: 死信队列消费者 利用监听器
 * @author: LiuZhuzheng
 * @create: 2021-12-03 16:56
 **/
@Component
public class DeadLetterQueueConsumer {
    /**
     * 接收消息
     * */
    @RabbitListener(queues = "dead_queue")
    public void receiveDeadMessage(Message message, Channel channel) throws Exception{
        String msg = new String(message.getBody());
        System.out.println("当前时间："+System.currentTimeMillis()+"，收到死信队列消息：{}"+msg);
    }
 
}