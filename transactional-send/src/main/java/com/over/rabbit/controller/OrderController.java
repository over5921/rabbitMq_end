package com.over.rabbit.controller;

import com.over.rabbit.pojo.Order;
import com.over.rabbit.service.MQOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: 学相伴-飞哥
 * @description: OrderController
 * @Date : 2021/3/6
 */
@RestController
public class OrderController {

    @Autowired
    private MQOrderService mqOrderService;

    @GetMapping("/test/order")
    public String testOrder() throws Exception {
        //订单生成
        String orderId = "1000001";
        Order orderInfo = new Order();
        orderInfo.setOrderId(orderId);
        orderInfo.setUserId(1);
        orderInfo.setOrderContent("买了一个方便面");
        mqOrderService.createOrder(orderInfo);
        System.out.println("订单创建成功.......");
        return "success";
    }
}
