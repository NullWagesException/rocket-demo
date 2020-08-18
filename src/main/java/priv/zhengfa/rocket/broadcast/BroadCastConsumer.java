package priv.zhengfa.rocket.broadcast;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import priv.zhengfa.rocket.config.JmsConfig;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 22:17
 * @Description:
 */
public class BroadCastConsumer {

    private DefaultMQPushConsumer consumer;

    public BroadCastConsumer() throws Exception {
        consumer = new DefaultMQPushConsumer(JmsConfig.BROAD_GROUP);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.subscribe(JmsConfig.TOPIC_BROAD_CAST, "broad-message-1");
        // 设置全量消费
        consumer.setMessageModel(MessageModel.BROADCASTING);
        // 设置集群消费
//        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.setPullInterval(1000);
        consumer.setPullBatchSize(1);
        consumer.setConsumeMessageBatchMaxSize(10);
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
        System.out.println("consumer启动，全量消费模式...");
    }

    public static void main(String[] args) throws Exception {
        new BroadCastConsumer();
    }

}
