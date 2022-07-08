package com.over.rabbit.service.impl;

import com.over.rabbit.service.ReceiveService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jsflz on 2022/7/7 9:54
 */
@Service("receiveService")
public class ReceiveServiceImpl implements ReceiveService {

    //注入Amqp的模板类，利用这个对象来发送和接收消息
    @Resource
    private AmqpTemplate amqpTemplate;
    /**
     * 这里个接收不是不间断接收消息，每执行一次这个方法只能接收一次消息，如果有新消息进入则不会自动接收消息
     * 不建议使用
     */
    public void receive() {
//        String message= (String) amqpTemplate.receiveAndConvert("bootDirectQueue");
//        System.out.println(message);
    }

    @RabbitListener(queues = {"bootDirectQueue"})
    @Override
    public void directReceive(String message) {
        System.out.println("监听器接收的消息----"+message);
    }

//    @RabbitListener(queues ="fanoutQueue")
//    @Override
//    public void fanoutReceive(String message){
//        System.out.println("Boot的Fanout消息----"+message);
//    }

        @RabbitListener(bindings={
                            @QueueBinding(//@QueueBinding注解要完成队列和交换机的
                                          value = @Queue(),//@Queue创建一个队列（没有指定参数则表示创建一个随机队列）
                                          exchange=@Exchange(name="fanoutExchange",type="fanout")//创建一个交换机
                                          )
                            }
                   )
    public void fanoutReceive01(String message){
        System.out.println("fanoutReceive01监听器接收的消息----"+message);
    }



    @RabbitListener(bindings={
            @QueueBinding(//@QueueBinding注解要完成队列和交换机的
                    value = @Queue(),//@Queue创建一个队列（没有指定参数则表示创建一个随机队列）
                    exchange=@Exchange(name="fanoutExchange",type="fanout")//创建一个交换机
            )
    }
    )
    public void fanoutReceive02(String message){
        System.out.println("fanoutReceive02监听器接收的消息----"+message);
    }

        @RabbitListener(bindings = {@QueueBinding(value=@Queue("topic01"),key = {"aa"},exchange =@Exchange(name = "topicExchange",type = "topic"))})
    public void  topicReceive01(String message){
        System.out.println("topic01消费者 ---aa---"+message );
    }
    @RabbitListener(bindings = {@QueueBinding(value=@Queue("topic02"),key = {"aa.*"},exchange =@Exchange(name = "topicExchange",type = "topic"))})
    public void  topicReceive02(String message){
        System.out.println("topic02消费者 ---aa.*---"+message );
    }
    @RabbitListener(bindings = {@QueueBinding(value=@Queue("topic03"),key = {"aa.#"},exchange =@Exchange(name = "topicExchange",type = "topic"))})
    public void  topicReceive03(String message){
        System.out.println("topic03消费者 ---aa。#---"+message );
    }
}
