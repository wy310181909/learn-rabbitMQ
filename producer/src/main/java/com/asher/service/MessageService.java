package com.asher.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class MessageService {

    final RabbitTemplate rabbitTemplate;

    public MessageService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }


    public void sendMessage() throws IOException, InterruptedException {

        //设置返回机制的回调函数
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息{}路由失败，失败原因：{}", message.getMessageProperties().getMessageId(), replyText);
        });

        //设置确认机制的回调函数
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info(correlationData != null ? correlationData.getId() : "null" + "发送成功");
            } else {
                log.error(correlationData != null ? correlationData.getId() : "null" + "发送失败， 原因： " + cause);
            }
        });

        //设置过期时间
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setExpiration("15000");

        String s = "message";
        Message message = new Message(s.getBytes(), messageProperties);

        //设置id在消息确认机制可以识别发送的是什么消息
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("uuid");


        rabbitTemplate.send(
                "exchange.order.restaurant",
                "key.restaurant",
                message, correlationData
        );

//        rabbitTemplate.convertAndSend(
//                "exchange.order.restaurant",
//                "key.restaurant",
//                messageToSend,correlationData);

        //        rabbitTemplate.execute(channel -> {
        //            channel.abort();
        //            return null;
        //        });

        Thread.sleep(1000);

    }
}


