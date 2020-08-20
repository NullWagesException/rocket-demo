package priv.zhengfa.rocket.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import priv.zhengfa.rocket.config.JmsConfig;
import priv.zhengfa.rocket.easy.EasyConsumer;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 22:17
 * @Description: 顺序消费
 */
public class OrderedConsumer {

    public OrderedConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(JmsConfig.ORDER_GROUP);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.subscribe(JmsConfig.TOPIC_ORDER, "order_message");
        consumer.registerMessageListener((MessageListenerOrderly) (list, consumeOrderlyContext) -> {
            consumeOrderlyContext.setAutoCommit(true);
            for (MessageExt msg : list) {
                String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                System.out.println(body);
            }
            return ConsumeOrderlyStatus.SUCCESS;
        });
        consumer.start();
        System.out.println("顺序消费者启动...");
    }

    public static void main(String[] args) throws Exception {
        new OrderedConsumer();
    }

}
