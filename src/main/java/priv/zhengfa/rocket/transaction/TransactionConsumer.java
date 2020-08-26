package priv.zhengfa.rocket.transaction;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;
import priv.zhengfa.rocket.config.JmsConfig;
import priv.zhengfa.rocket.easy.EasyConsumer;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

/**
 * @Author: zhengfa
 * @Date: 2020/8/19 14:24
 * @Description:
 */
public class TransactionConsumer {

    public TransactionConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(JmsConfig.TRANSACTION_GROUP);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.subscribe(JmsConfig.TOPIC_TRANSACTION, "transaction");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
            for (Message msg : list) {
                //消费者获取消息 这里只输出 不做后面逻辑处理
                System.out.println("---------消费消息：" + LocalTime.now());
                String body = new String(msg.getBody(), StandardCharsets.UTF_8);
                System.out.println(body);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.println("consumer启动...");
    }

    public static void main(String[] args) throws Exception {
        new TransactionConsumer();
    }

}
