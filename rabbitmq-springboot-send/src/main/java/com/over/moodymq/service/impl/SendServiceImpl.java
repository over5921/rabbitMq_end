package com.over.moodymq.service.impl;

import com.over.moodymq.service.SendService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * Created by jsflz on 2022/7/7 9:39
 */
@Service("sendService")
public class SendServiceImpl implements SendService {

    //注入Amqp的模板类，利用这个对象来发送和接收消息
    @Resource
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息
     * 参数 1 为交换机名
     * 参数 2 为RoutingKey
     * 参数 3 为我们的具体发送的消息数据
     */
    @Override
    public void sendMessage(String message) {
        amqpTemplate.convertAndSend("bootDirectExchange","bootDirectRoutingKey",message);

    }

    @Override
    public void sendFanoutMessage(String message) {
        amqpTemplate.convertAndSend("fanoutExchange","",message);
    }

    @Override
    public void sendTopicMessage(String message) {
        amqpTemplate.convertAndSend("topicExchange","aa.bb.cc",message);
    }

    @Override
    public void sendConfimDirect(String confimDirict) {
//        调用接口，查看rabbitmq-provuder项目的控制台输出情况（原因里面有说，没有找到交换机'non-existent-exchange'）：
//        所以证明在交换器没有配置，推送消息找不到对应的交换器，会执行ConfirmCallback方法。
        amqpTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", "confimDirict数据");
    }

    @Override
    public void sendConfimDirect1(String confimDirict1) {
//        可以看到ReturnCallback和ConfirmCallback这两个都被调用了。
//
//        CallfirmCallback的确认情况为true，因为消息是推送成功到服务器了的，所以ConfirmCallback对消息确认情况是true
//
//                ReturnCallback的回应消息为NO_ROUTE找不到队列
//
//③消息推送到sever，交换机和队列啥都没找到
//
//        这种情况和1、2很相似，会调用ReturnCallback和CallfirmCallback这两个回调函数
        amqpTemplate.convertAndSend("lonelyDirectExchange", "TestDirectRouting", "confimDirict数据");
    }

    // //显而易见，该方法存在一个缺陷，就是一个ttl时间对应一个队列，每增加一个ttl时间的需求就需要增加一个队列
    @Override
    public void sendDDL() {
        System.out.println("当前时间："+System.currentTimeMillis()+"，发送一条消息给两个队列："+"消息！");
        amqpTemplate.convertAndSend("normal_exchange","normal_routing_key_a","延时10秒的队列a" + "消息！");
        amqpTemplate.convertAndSend("normal_exchange","normal_routing_key_b","延时40秒的队列a" + "消息！");
    }

    ////        ttl死信队列优化
    ////        针对上述问题，创建一个新队列c（上述代码已有），在初始化队列时不设置ttl时间，而是设置消息的ttl过期时间，其控制器代码如下
    @Override
    public void sendDDLYh() {
        String ttl="20000";
        String msg ="优化后的延迟消息";
        System.out.println("当前时间："+Instant.now()+"，"+ttl+"毫秒后发送消息："+msg);
        amqpTemplate.convertAndSend("normal_exchange", "normal_routing_key_c", msg, message -> {
            message.getMessageProperties().setExpiration(ttl);
            return message;
        });
    }
}
