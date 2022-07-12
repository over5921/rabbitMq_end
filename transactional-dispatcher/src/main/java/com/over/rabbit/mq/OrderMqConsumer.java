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

import java.io.IOException;

/**
 * @author: 学相伴-飞哥
 * @description: OrderMqConsumer
 * @Date : 2021/3/6
 */
@Service
public class OrderMqConsumer {

    @Autowired
    private DispatchService dispatchService;

    private int count = 1;


    // 解决消息重试的集中方案：
    // 1: 控制重发的次数 + 死信队列
    // 2: try+catch+手动ack
    // 3: try+catch+手动ack + 死信队列处理 + 人工干预
    @RabbitListener(queues = {"order.queue"})
    public void messageconsumer(String ordermsg, Channel channel,
                                CorrelationData correlationData,
                                @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws Exception {
        try {
            // 1:获取消息队列的消息
            System.out.println("收到MQ的消息是: " + ordermsg + ",count = " + count++);
            //  2: 获取订单服务的信息
            Order order = JsonUtil.string2Obj(ordermsg, Order.class);
            // 3: 获取订单id
            String orderId = order.getOrderId();
            // 4：保存运单
            int count = dispatchService.countOrderById(orderId);
            // 4：保存运单
            if(count==0){
                dispatchService.dispatch(orderId);
            }
            if(count>0){
                int i = dispatchService.updateDispatch(orderId, order);
                if (i>0){
                    System.out.println("修改成功！");
                }
            }

            System.out.println(1 / 0); //出现异常

            channel.basicAck(tag, false);
        } catch (Exception ex) {
            //如果出现异常的情况下,根据实际的情况去进行重发
            //重发一次后，丢失，还是日记，存库根据自己的业务场景去决定
            //参数1：消息的tag  参数2：false 多条处理 参数3：requeue 重发
            // false 不会重发，会把消息打入到死信队列
            // true 的会会死循环的重发，建议如果使用true的话，不加try/catch否则就会造成死循环

            // 单条，不重发 达到死信队列！
            channel.basicNack(tag, false, false);// 死信队列
        }
    }

}
