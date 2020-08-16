package priv.zhengfa.rocket.easy;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import priv.zhengfa.rocket.config.JmsConfig;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 22:17
 * @Description:
 */
public class EasyConsumer {

    private DefaultMQPushConsumer consumer;

    public EasyConsumer() throws Exception {
        consumer = new DefaultMQPushConsumer(JmsConfig.GROUP);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.subscribe(JmsConfig.TOPIC_EASY, "easy-message-1");
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
        new EasyConsumer();
    }

}
