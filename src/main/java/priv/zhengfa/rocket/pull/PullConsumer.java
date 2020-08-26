package priv.zhengfa.rocket.pull;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.consumer.PullResult;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import priv.zhengfa.rocket.config.JmsConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-16 23:15
 * @Description:
 */
public class PullConsumer {

    private DefaultMQPullConsumer consumer;

    private static final Map<MessageQueue, Long> offsetTable = new HashMap<MessageQueue, Long>();

    public PullConsumer() throws Exception {
        consumer = new DefaultMQPullConsumer(JmsConfig.GROUP);
        consumer.setNamesrvAddr(JmsConfig.NAME_SERVER);
        consumer.start();

        Set<MessageQueue> mqs = consumer.fetchSubscribeMessageQueues("TopicStudent");
        for (MessageQueue mq : mqs) {
            System.err.println("Consume from the queue: " + mq);
            SINGLE_MQ:
            while (true){
                try {
                    PullResult pullResult = consumer.pullBlockIfNotFound(mq, null, getMessageQueueOffset(mq), 32);
                    System.out.println(pullResult);
                    putMessageQueueOffset(mq, pullResult.getNextBeginOffset());

                    switch (pullResult.getPullStatus()) {
                        case FOUND:
                            List<MessageExt> messageExtList = pullResult.getMsgFoundList();
                            for (MessageExt m : messageExtList) {
                                System.out.println(new String(m.getBody()));
                            }
                            break;
                        case NO_NEW_MSG:
                            break SINGLE_MQ;
                        default:
                            break;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        consumer.shutdown();
    }

    private static void putMessageQueueOffset(MessageQueue mq, long offset) {
        offsetTable.put(mq, offset);
    }

    private static long getMessageQueueOffset(MessageQueue mq) {
        Long offset = offsetTable.get(mq);
        if (offset != null) {
            return offset;
        }
        return 0;
    }

}
