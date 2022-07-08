package com.over.rabbit;


import com.over.rabbit.service.ReceiveService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class Application_resive {

    public static void main(String[] args) {
        ApplicationContext ac=SpringApplication.run(Application_resive.class, args);
        ReceiveService service= (ReceiveService) ac.getBean("receiveService");
        //使用了消息监听器接收消息那么就不需要调用接收方法来接收消息
//        service.receive();


    }

}
