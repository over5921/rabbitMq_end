package com.over.rabbit.mq;

import com.over.rabbit.pojo.Order;
import com.over.rabbit.util.JsonUtil;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.tools.json.JSONUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author: 学相伴-飞哥
 * @description: MQService
 * @Date : 2021/3/6
 */
@Component
public class OrderMQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    //@PostConstruct注解好多人以为是Spring提供的。其实是Java自己的注解。
    //Java中该注解的说明：@PostConstruct该注解被用来修饰一个非静态的void（）方法。被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，
    // 并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。

    /**
     * 对象在spring容器中被创建完成后，初始化方法之前调用！
     */
    @PostConstruct
    public void regCallback() {
        // 消息发送成功以后，给予生产者的消息回执,来确保生产者的可靠性
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("cause:"+cause);
                System.out.println("ack:"+ack);
                // 如果ack为true代表消息已经收到
                String orderId = correlationData.getId();

                if (!ack) {
                    // 这里可能要进行其他的方式进行存储
                    System.out.println("MQ队列应答失败，orderId是:" + orderId);
                    return;
                }

                try {
                    String updatesql = "update ksd_order_message set status = 1 where order_id = ?";
                    int count = jdbcTemplate.update(updatesql, orderId);
                    if (count == 1) {
                        System.out.println("本地消息状态修改成功，消息成功投递到消息队列中...");
                    }
                } catch (Exception ex) {
                    System.out.println("本地消息状态修改失败，出现异常：" + ex.getMessage());
                }
            }
        });
    }


    public void sendMessage(Order order) {
        // 通过MQ发送消息
        rabbitTemplate.convertAndSend("order_fanout_exchange", "", JsonUtil.obj2String(order),
                new CorrelationData(order.getOrderId()));
    }

}
