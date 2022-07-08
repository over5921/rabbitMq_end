package com.over.rabbit.exchange.direct;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * direct 类型接收消息
 */
public class Receive {

    public static void main(String[] args) {
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection=null;
        Channel channel=null;

        try {
            connection=factory.newConnection();
            channel=connection.createChannel();

            channel.queueDeclare("myDirectQueue",true,false,false,null);
            channel.exchangeDeclare("directExchange","direct",true);
            channel.queueBind("myDirectQueue","directExchange","directRoutingKey");
            /**
             * 监听某个队列并获取队列中的数据
             * 注意：
             *   当前被讲定的队列必须已经存在并正确的绑定到了某个交换机中
             */
            channel.basicConsume("myDirectQueue",true,"",new DefaultConsumer(channel){
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message=new String(body);
                    System.out.println("消费者 ---"+message);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
