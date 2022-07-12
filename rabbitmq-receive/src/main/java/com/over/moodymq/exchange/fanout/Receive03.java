package com.over.moodymq.exchange.fanout;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Receive03 {
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
            /**
             * 明确指定的队列名称并进行了与交换机的绑定，可以保证fanout类型的的消息不会丢失
             * 但是这么做没有任何的意义因为消费者最终的数量可能会很对因此不能让所有的消费者全部
             * 监听统一个队列
             */
            channel.queueDeclare("fanoutQueue",true,false,false,null);
            channel.exchangeDeclare("fanoutExchange","fanout",true);
            //将这个随机的队列绑定到交换机中， 由于是fanout类型的交换机因此不需指定RoutingKey进行绑定
            channel.queueBind("fanoutQueue","fanoutExchange","");
            /**
             * 监听某个队列并获取队列中的数据
             * 注意：
             *   当前被讲定的队列必须已经存在并正确的绑定到了某个交换机中
             */
            channel.basicConsume("fanoutQueue",true,"",new DefaultConsumer(channel){
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message=new String(body);
                    System.out.println("Receive03消费者 ---"+message);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
