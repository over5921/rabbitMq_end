package com.over.rabbit.service;

import com.over.rabbit.dao.OrderDataBaseService;
import com.over.rabbit.mq.OrderMQService;
import com.over.rabbit.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MQOrderService {


	@Autowired
	private OrderDataBaseService orderDataBaseService;

	@Autowired
	private OrderMQService orderMQService;

	// 创建订单
	public void createOrder(Order orderInfo) throws Exception {
		// 1: 订单信息--插入丁订单系统，订单数据库事务
		orderDataBaseService.saveOrder(orderInfo);

		// 2：通過 消息队列 接口发送订单信息到运单系统
		orderMQService.sendMessage(orderInfo);
	}

	public List<Order> selectOrderMessage() {

		List<Order> orders = orderDataBaseService.selectStatusMessages();
		return orders;

	}
}
