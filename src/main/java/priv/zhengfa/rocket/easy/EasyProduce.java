package priv.zhengfa.rocket.easy;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;
import priv.zhengfa.rocket.config.JmsConfig;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 22:28
 * @Description:
 */
@Component
public class EasyProduce {

    private DefaultMQProducer producer;

    public EasyProduce() throws Exception {
        producer = new DefaultMQProducer("easy-rocket");
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        //不开启vip通道 开通口端口会减2
        producer.setVipChannelEnabled(false);
        producer.start();
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }

    public void setProducer(DefaultMQProducer producer) {
        this.producer = producer;
    }
}
