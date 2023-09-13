package com.delin.skywalking.service.impl;

import com.delin.skywalking.service.DemoService;
import com.delin.skywalking.service.HelloService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.apache.skywalking.apm.toolkit.trace.TraceCrossThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HelloService
 *
 * @author: chendl
 * @date: Created in 2023/9/8 16:53
 * @description: com.delin.skywalking.service.impl.HelloService
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HelloServiceImpl implements HelloService {

    private final DemoService demoService;

    public void helloTrace() {
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        log.info("invoke HelloService helloTrace method threadId:{} threadName:{}", threadId, threadName);
    }

    @Override
    public void asyncHelloTrace() {
        log.info("invoke method trace id normal print threadId:{} threadName:{}", Thread.currentThread().getId(), Thread.currentThread().getName());
        System.out.println("======asyncHelloTrace======");
        Executors.newSingleThreadExecutor().execute(new RunnableWrapper(() -> {
            long threadId = Thread.currentThread().getId();
            String threadName = Thread.currentThread().getName();
            log.info("invoke HelloService asyncHelloTrace method threadId:{} threadName:{}", threadId, threadName);
        }));
    }

    @Override
    public void traceIdDropRepetition() {
        String mainThreadName = Thread.currentThread().getName();
        long mainThreadId = Thread.currentThread().getId();
        log.info("main thread id:{} name:{}", mainThreadId, mainThreadName);
        demoService.asyncInvokePrint();
    }
}
