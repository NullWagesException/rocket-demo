package priv.zhengfa.rocket.controller;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.zhengfa.rocket.config.JmsConfig;
import priv.zhengfa.rocket.easy.EasyProduce;

import java.time.LocalTime;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 9:35
 * @Description:
 */
@RestController
public class RocketController {

    @Autowired
    EasyProduce produce;

    @GetMapping("/test")
    public String test() throws Exception {
        DefaultMQProducer mqProducer = produce.getProducer();
        //创建生产信息
        for (int i = 0; i < 1; i++) {
            Message message = new Message(JmsConfig.TOPIC_EASY, "easy-message-1", ("easy message：" + i).getBytes());
            SendResult send = mqProducer.send(message);
            System.out.println("---------生产消息：" + LocalTime.now().toString());
            System.out.println(send.toString());
        }
        return "ok";
    }


}
