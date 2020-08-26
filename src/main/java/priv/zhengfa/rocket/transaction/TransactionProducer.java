package priv.zhengfa.rocket.transaction;

import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import priv.zhengfa.rocket.config.JmsConfig;

import javax.annotation.PostConstruct;

/**
 * @Author: zhengfa
 * @Date: 2020/8/19 14:04
 * @Description:
 */
public class TransactionProducer {

    private TransactionMQProducer producer;

    private final OrderTransactionListener transactionListener = new OrderTransactionListener();

    @PostConstruct
    public void init() throws Exception{
        producer = new TransactionMQProducer(JmsConfig.TRANSACTION_GROUP);
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        //不开启vip通道 开通口端口会减2
        producer.setVipChannelEnabled(false);

        // 添加事务监听
        producer.setTransactionListener(transactionListener);
        producer.start();
    }

    public TransactionMQProducer getProducer() {
        return producer;
    }

    public void setProducer(TransactionMQProducer producer) {
        this.producer = producer;
    }

    public static void main(String[] args) throws Exception{
        TransactionProducer transactionProducer = new TransactionProducer();
        TransactionMQProducer producer = transactionProducer.getProducer();
        //创建生产信息
        for (int i = 1; i <= 3; i++) {
            Message message = new Message(JmsConfig.TOPIC_TRANSACTION, "transaction", ("" + i).getBytes());
            TransactionSendResult result = producer.sendMessageInTransaction(message, 1);
            System.out.println(result.getLocalTransactionState().toString());
        }
    }
}
