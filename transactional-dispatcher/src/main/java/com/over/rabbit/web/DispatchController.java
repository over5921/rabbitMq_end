package com.over.rabbit.web;

import com.over.rabbit.service.DispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dispatch")
public class DispatchController {


	@Autowired
	public DispatchService dispathService;



	// 添加订单后，添加调度信息
	@GetMapping("/order")
	public String lock(String orderId) throws Exception {

		// 接口调用超时，会产生 配送表，
		// 订单表因为有事务回滚，没有产生订单表！
//		if(orderId.equals("1000001")) {
//			Thread.sleep(3000L); // 模拟业务耗时，接口调用者会认为超时
//		}

		dispathService.dispatch(orderId); // 将外卖订单分配给小哥
		return "success";
	}
	
	
}
