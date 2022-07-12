package com.over.moodymq;


import com.over.moodymq.service.SendService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        ApplicationContext ac=SpringApplication.run(Application.class, args);
        SendService service= (SendService) ac.getBean("sendService");

//        service.sendMessage("Boot的测试数据");
//        service.sendFanoutMessage("Boot的Fanout测试数据");
//        service.sendTopicMessage("Boot的Topic测数据数据key 为 aa.bb.cc");
//        service.sendConfimDirect("confimDirict数据");
//        service.sendConfimDirect1("confimDirict1数据");

//        service.sendDDL();

        service.sendDDLYh();

    }

}
