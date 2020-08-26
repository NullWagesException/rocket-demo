package priv.zhengfa.rocket.easy;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import priv.zhengfa.rocket.config.JmsConfig;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.List;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 22:17
 * @Description:
 */
@RocketMQMessageListener(topic = "topic_easy", consumerGroup = "easy-rocket")
@Service
public class EasyConsumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        System.out.println("消费消息:" + s);
    }
}
