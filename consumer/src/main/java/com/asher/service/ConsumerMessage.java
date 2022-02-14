package com.asher.service;

import com.asher.consist.RabbitConsts;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class ConsumerMessage {
    @RabbitListener(bindings = {
            @QueueBinding(
                    value = @Queue(name = RabbitConsts.QUEUE_TEST,
                            arguments = {
                                    //                                            @Argument(name =
                                    //                                            "x-message-ttl", value =
                                    //                                            "1000", type = "java.lang
                                    //                                            .Integer"),
                                    //                                            @Argument(name =
                                    //                                            "x-dead-letter-exchange",
                                    //                                            value = "aaaaa"),
                                    //                                            @Argument(name =
                                    //                                            "x-dead-letter-routing-key", value
                                    //                                            = "bbbb")
                            }),
                    exchange = @Exchange(name = RabbitConsts.EXCHANGE_TEST, type = ExchangeTypes.DIRECT),
                    key = "key.test"
            )
    })
    public void releaseCouponRecord(String body, Message message, Channel channel) throws IOException {
        long msgTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("msgTag=" + msgTag);
        System.out.println("message=" + message.toString());
        System.out.println("body=" + body);
        //复杂业务逻辑 TODO

        //告诉broker，消息已经被确认
        //成功确认，使用此回执方法后，消息会被 rabbitmq broker 删除
        channel.basicAck(msgTag, false);

        //告诉broker，消息拒绝确认,并重回队列
//        channel.basicNack(msgTag,false,true);
        //告诉broker，消息拒绝确认,并不重回队列
//        channel.basicNack(msgTag,false,false);
    }
}
