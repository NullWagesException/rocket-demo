package priv.zhengfa.rocket.thread;

import java.util.concurrent.*;

/**
 * @Author: nullWagesException
 * @Date: 2020-08-15 9:52
 * @Description:
 */
public class ThreadPool {

    // 获取一个默认的定长线程池
    // 配置9个核心线程，最大线程数量为20，阻塞队列长度为20，
    // 也就是说该线程池最高负载为：9+20+(20-9)
    public static Executor inline_executor = new ThreadPoolExecutor(9, 20,
            60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20),new PoolFactory("inline_order"));

    public static Executor offline_executor = new ThreadPoolExecutor(9, 20,
            60, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20),new PoolFactory("offline_order"));

}

class PoolFactory implements ThreadFactory {

    private final String factoryName;

    public PoolFactory(String name){
        factoryName = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, factoryName);
    }
}
