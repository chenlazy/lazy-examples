package transmittable;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.threadpool.TtlExecutors;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * TransmittableExample
 * ThreadLocal：父子线程不会传递threadLocal副本到子线程中
 * InheritableThreadLocal：在子线程创建的时候，父线程会把threadLocal拷贝到子线中（但是线程池的子线程不会频繁创建，就不会传递信息）
 * TransmittableThreadLocal：解决了2中线程池无法传递线程本地副本的问题，在构造类似Runnable接口对象时进行初始化。
 *
 * @author: chendl
 * @date: Created in 2023/9/8 16:30
 * @description: com.lazy.example.transmittable.TransmittableExample
 */
public class TransmittableExampleTest {

    @Test
    void threadLocalTest() throws InterruptedException {
        threadLocal();
        System.out.println("============================");
        System.out.println();

        // 2. ITL测试
        itlTest();

        itlTestThreadPoolTest();
        System.out.println("============================");
        System.out.println();

        ttlTest();
    }

    private static void ttlTest() throws InterruptedException {
        TransmittableThreadLocal<String> local = new TransmittableThreadLocal<>();
        local.set("我是主线程");
        //生成额外的代理
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        //**核心装饰代码！！！！！！！！！**
        executorService = TtlExecutors.getTtlExecutorService(executorService);
        CountDownLatch c1 = new CountDownLatch(1);
        CountDownLatch c2 = new CountDownLatch(1);
        executorService.submit(() -> {
            System.out.println("我是线程1：" + local.get());
            c1.countDown();
        });
        c1.await();
        local.set("修改主线程");
        System.out.println(local.get());
        executorService.submit(() -> {
            System.out.println("我是线程2：" + local.get());
            c2.countDown();
        });
        c2.await();
    }

    // ITL测试
    private static void itlTestThreadPoolTest() {
        ThreadLocal<String> local = new InheritableThreadLocal<>();
        try {
            local.set("-------------Master Thread-------------");
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            CountDownLatch c1 = new CountDownLatch(1);
            CountDownLatch c2 = new CountDownLatch(1);
            //初始化init的时候，赋予了父线程的ThreadLocal的值
            executorService.execute(() -> {
                System.out.println("线程1" + local.get());
                c1.countDown();
            });
            c1.await();
            //主线程修改值
            local.set("修改主线程");
            //再次调用，查看效果
            executorService.execute(() -> {
                System.out.println("线程2" + local.get());
                c2.countDown();
            });
            c2.await();
            executorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //使用完毕，清除线程中ThreadLocalMap中的key。
            local.remove();
        }
    }

    private static void itlTest() throws InterruptedException {
        ThreadLocal<String> local = new InheritableThreadLocal<>();
        local.set("-------------Master Thread-------------");
        Executors.newFixedThreadPool(1).execute(() -> System.out.println("-------------Child Thread-------------\n" + local.get()));
        Thread.sleep(2000);
    }

    // ThreadLocal测试
    private static void threadLocal() {
        ThreadLocal<String> local = new ThreadLocal<>();
        try {
            local.set("-------------Master Thread-------------");
            ExecutorService executorService = Executors.newFixedThreadPool(1);
            CountDownLatch c1 = new CountDownLatch(1);
            CountDownLatch c2 = new CountDownLatch(1);
            executorService.execute(() -> {
                System.out.println("Thread A:" + local.get());
                c1.countDown();
            });
            c1.await();
            executorService.execute(() -> {
                System.out.println("Thread B:" + local.get());
                c2.countDown();
            });
            c2.await();
            executorService.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //使用完毕，清除线程中ThreadLocalMap中的key。
            local.remove();
        }
    }
}
