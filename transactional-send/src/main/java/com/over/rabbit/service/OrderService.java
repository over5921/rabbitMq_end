package com.over.rabbit.service;

import com.over.rabbit.dao.OrderDataBaseService;
import com.over.rabbit.pojo.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * RestTemplate实现两个服务的交互！
 */

@Component
public class OrderService {


	@Autowired
	private OrderDataBaseService orderDataBaseService;

	// 创建订单
	@Transactional(rollbackFor = Exception.class) // 订单创建整个方法添加事务 acid
	public void createOrder(Order orderInfo) throws Exception {

		// 1: 订单信息--插入丁订单系统，订单数据库事务
		orderDataBaseService.saveOrder(orderInfo);

		// 2：通過Http接口发送订单信息到运单系统
		String result = dispatchHttpApi(orderInfo.getOrderId());
		if(!"success".equals(result)) {
			throw new Exception("订单创建失败,原因是运单接口调用失败!");
		}
	}

	/**
	 *  模拟http请求接口发送，运单系统，将订单号传过去 springcloud
	 * @return
	 */
	private String dispatchHttpApi(String orderId) {
		SimpleClientHttpRequestFactory factory  = new SimpleClientHttpRequestFactory();
		// 链接超时 > 3秒
		factory.setConnectTimeout(3000);  //设置
		// 处理超时 > 2秒
		factory.setReadTimeout(2000);  // 设置
		// 发送http请求
		String url = "http://localhost:9000/dispatch/order?orderId="+orderId;
		RestTemplate restTemplate = new RestTemplate(factory);//异常
		String result = restTemplate.getForObject(url, String.class);
		return result;
	}


}
