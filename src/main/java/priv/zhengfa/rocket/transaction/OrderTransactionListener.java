package priv.zhengfa.rocket.transaction;

import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;

/**
 * @Author: zhengfa
 * @Date: 2020/8/19 13:46
 * @Description:
 */
@Component
public class OrderTransactionListener implements TransactionListener {

    /**
     * 如果消息发送成功，就会调用到这里的executeLocalTransaction方法，来执行本地事务
     */
    @Override
    public LocalTransactionState executeLocalTransaction(Message message, Object o) {
        String str = new String(message.getBody(), StandardCharsets.UTF_8);
        // 代表事务成功
        if ("1".equals(str)){
            System.out.println("事务成功" + LocalTime.now());
            return LocalTransactionState.COMMIT_MESSAGE;
        }
        // 代表事务失败
        if ("2".equals(str)){
            System.out.println("事务失败" + LocalTime.now());
            return LocalTransactionState.ROLLBACK_MESSAGE;
        }
        // 代表事务状态未知，需要回查机制
        if ("3".equals(str)){
            System.out.println("事务未知，等待回查" + LocalTime.now());
            return LocalTransactionState.UNKNOW;
        }
        return null;
    }

    /**
     * 用于事务状态查询,如果可以查询到结果，就提交事务消息；如果没有查询到，就返回未知状态。
     * RocketMQ Broker服务器会以1分钟的间隔时间不断回查，直至达到事务回查最大检测数，
     * 如果超过这个数字还未查询到事务状态，则回滚此消息。
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
        System.out.println("事务回查" + LocalTime.now());
        return LocalTransactionState.UNKNOW;
    }
}
