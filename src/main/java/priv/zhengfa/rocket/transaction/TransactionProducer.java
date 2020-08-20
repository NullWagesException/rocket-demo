package priv.zhengfa.rocket.transaction;

import org.apache.rocketmq.client.producer.TransactionMQProducer;
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

    @Autowired
    private OrderTransactionListener transactionListener;

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
}
