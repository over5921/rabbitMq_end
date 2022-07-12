package com.over.rabbit.mq;

import com.over.rabbit.pojo.Order;
import com.over.rabbit.service.DispatchService;
import com.over.rabbit.util.JsonUtil;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

/**
 * @author: 学相伴-飞哥
 * @description: OrderMqConsumer
 * @Date : 2021/3/6
 */
@Service
public class DeadMqConsumer {

    @Autowired
    private DispatchService dispatchService;



    // 解决消息重试的集中方案：
    // 1: 控制重发的次数 + 死信队列
    // 2: try+catch+手动ack
    // 3: try+catch+手动ack + 死信队列处理 + 人工干预
    @RabbitListener(queues = {"dead.order.queue"})
    public void messageconsumer(String ordermsg, Channel channel,
                                CorrelationData correlationData,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            // 1:获取消息队列的消息
            System.out.println("收到MQ的消息是: " + ordermsg );
            //  2: 获取订单服务的信息
            Order order = JsonUtil.string2Obj(ordermsg, Order.class);
            // 3: 获取订单id
            String orderId = order.getOrderId();
            // 幂等性问题
            int count = dispatchService.countOrderById(orderId);
            // 4：保存运单
            if(count==0){
                dispatchService.dispatch(orderId);
            }
            if(count>0){
                System.out.println("ordddid"+orderId);
                System.out.println("order"+order);
                int i = dispatchService.updateDispatch(orderId, order);
                if (i>0){
                    System.out.println("修改成功！");
                }
            }


            // 3：手动ack告诉mq消息已经正常消费
            channel.basicAck(tag, false);
        } catch (Exception ex) {
            System.out.println("人工干预");
            System.out.println("发短信预警");
            System.out.println("同时把消息转移别的存储DB");
            channel.basicNack(tag, false,false);
        }
    }

}
