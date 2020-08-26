package priv.zhengfa.rocket.easy;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import priv.zhengfa.rocket.config.JmsConfig;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 22:28
 * @Description:
 */
@Configuration
public class EasyProduce {

    private final static String INIT = "start";
    private final static String DESTROY = "shutdown";

    @Bean
    public DefaultMQProducer mqProducer() {

        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        producer.setProducerGroup(JmsConfig.GROUP);
        /*发送失败不重试*/
        producer.setRetryAnotherBrokerWhenNotStoreOK(false);
        return producer;
    }

}
