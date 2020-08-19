package priv.zhengfa.rocket.controller;

import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import priv.zhengfa.rocket.broadcast.BroadCastProduce;
import priv.zhengfa.rocket.config.JmsConfig;
import priv.zhengfa.rocket.easy.EasyProduce;
import priv.zhengfa.rocket.order.OrderedProduce;
import priv.zhengfa.rocket.schedule.ScheduleProduce;
import priv.zhengfa.rocket.transaction.TransactionProducer;

import java.time.LocalTime;
import java.util.List;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 9:35
 * @Description:
 */
@RestController
public class RocketController {

    @Autowired
    EasyProduce produce;

    @Autowired
    BroadCastProduce broadCastProduce;

    @Autowired
    OrderedProduce orderedProduce;

    @Autowired
    ScheduleProduce scheduleProduce;

    @Autowired
    TransactionProducer transactionProducer;

    @GetMapping("/test")
    public String test() throws Exception {
        DefaultMQProducer mqProducer = produce.getProducer();
        //创建生产信息
        for (int i = 0; i < 10; i++) {
            Message message = new Message(JmsConfig.TOPIC_EASY, "easy-message-1", ("easy message：" + i).getBytes());
            SendResult send = mqProducer.send(message);
            System.out.println("---------生产消息：" + LocalTime.now().toString());
            System.out.println(send.toString());
        }

        return "ok";
    }

    @GetMapping("/test_asyn")
    public String testAsyn() throws Exception {
        DefaultMQProducer mqProducer = produce.getProducer();
        //创建生产信息
        for (int i = 0; i < 10; i++) {
            Message message = new Message(JmsConfig.TOPIC_EASY, "easy-message-1", ("easy message：" + i).getBytes());
            mqProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("---------生产消息：" + LocalTime.now().toString());
                    System.out.println(sendResult.toString());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(e.getMessage());
                }
            });

        }
        return "ok";
    }

    @GetMapping("/test_broad_cast")
    public String testBroadCast() throws Exception {
        DefaultMQProducer mqProducer = broadCastProduce.getProducer();
        //创建生产信息
        for (int i = 0; i < 100; i++) {
            Message message = new Message(JmsConfig.TOPIC_BROAD_CAST, "broad-message-1", ("broad message：" + i).getBytes());
            mqProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("---------生产消息：" + LocalTime.now().toString());
                    System.out.println(sendResult.toString());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(e.getMessage());
                }
            });

        }
        return "ok";
    }

    @GetMapping("/test_order")
    public String testOrder() throws Exception {
        DefaultMQProducer mqProducer = orderedProduce.getProducer();
        //创建生产信息
        for (int i = 0; i < 5; i++) {
            Message message = new Message(JmsConfig.TOPIC_ORDER, "order_message", ("创建订单：" + i).getBytes());
            mqProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    int id = (int) o;
                    int index = id % list.size();
                    return list.get(index);
                }
            },i);
        }

        for (int i = 0; i < 5; i++) {
            Message message = new Message(JmsConfig.TOPIC_ORDER, "order_message", ("支付订单：" + i).getBytes());
            mqProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    int id = (int) o;
                    int index = id % list.size();
                    return list.get( index);
                }
            },i);
        }

        for (int i = 0; i < 5; i++) {
            Message message = new Message(JmsConfig.TOPIC_ORDER, "order_message", ("发货：" + i).getBytes());
            mqProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                    int id = (int) o;
                    int index = id % list.size();
                    return list.get( index);
                }
            },i);
        }
        return "ok";
    }

    @GetMapping("/test_schedule")
    public String testSchedule() throws Exception {
        DefaultMQProducer mqProducer = scheduleProduce.getProducer();
        //创建生产信息
        for (int i = 0; i < 10; i++) {
            Message message = new Message(JmsConfig.TOPIC_SCHEDULE, "schedule_message", ("message：" + i).getBytes());
            // 设置延迟级别
            message.setDelayTimeLevel(3);
            mqProducer.send(message, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("---------生产消息：" + LocalTime.now().toString());
                    System.out.println(sendResult.toString());
                }

                @Override
                public void onException(Throwable e) {
                    System.out.println(e.getMessage());
                }
            });

        }
        return "ok";
    }

    @GetMapping("/test_transaction")
    public String testTransaction() throws Exception {
        TransactionMQProducer producer = transactionProducer.getProducer();
        //创建生产信息
        for (int i = 1; i <= 3; i++) {
            Message message = new Message(JmsConfig.TOPIC_TRANSACTION, "transaction", ("" + i).getBytes());
            producer.sendMessageInTransaction(message,i);

        }
        return "ok";
    }

}
