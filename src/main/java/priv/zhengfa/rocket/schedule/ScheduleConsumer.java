package priv.zhengfa.rocket.schedule;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.Message;
import priv.zhengfa.rocket.config.JmsConfig;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

/**
 * @Author: zhengfa
 * @Date: 2020/8/18 17:12
 * @Description: 延时模式消费者
 */
public class ScheduleConsumer {

    private DefaultMQPushConsumer consumer;

    public ScheduleConsumer() throws Exception {
        consumer = new DefaultMQPushConsumer(JmsConfig.SCHEDULE_GROUP);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.subscribe(JmsConfig.TOPIC_SCHEDULE, "schedule_message");
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
        new ScheduleConsumer();
    }

}
