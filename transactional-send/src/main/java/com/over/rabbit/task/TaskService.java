package com.over.rabbit.task;

import com.over.rabbit.pojo.Order;
import com.over.rabbit.service.MQOrderService;
import com.over.rabbit.util.JsonUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: xuke
 * @time: 2021/3/7 16:07
 */

/**
 * 定时任务
 * 当 rabbitmq 出现宕机之后，将数据库 冗余表中的 status=0的消息查询出来，重写投递到交换机中！
 */
@Service
public class TaskService {

    private int count = 0;

    @Autowired
    private MQOrderService mqOrderService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Scheduled(cron = "*/5 * * * * ?")  // 每隔5s发一次！
    public void sendMessage() {
        // 把消息为0的状态消息重新查询出来，投递到MQ中。
        count++;
        if (count < 5) {
            List<Order> orderList = mqOrderService.selectOrderMessage();
            for (Order order : orderList) {
                System.out.println("定时任务:" + order);
                rabbitTemplate.convertAndSend("order-fanout_exchange", "", JsonUtil.obj2String(order));
                break;
            }
        }
    }

}