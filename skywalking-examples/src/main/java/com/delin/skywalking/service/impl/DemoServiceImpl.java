package com.delin.skywalking.service.impl;

import com.delin.skywalking.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * DemoService
 *
 * @author: chendl
 * @date: Created in 2023/9/8 16:54
 * @description: com.delin.skywalking.service.impl.DemoService
 */
@Slf4j
@Service
public class DemoServiceImpl implements DemoService {


    public void traceDemo() {
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        log.info("invoke DemoService traceDemo method threadId:{} threadName:{}", threadId, threadName);
    }

    @Override
    public void asyncInvokePrint() {
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        log.info("invoke async thread id:{} name:{}", threadId, threadName);
        CompletableFuture.runAsync(() -> {
            log.info("async thread id:{} name:{}", Thread.currentThread().getId(), Thread.currentThread().getName());
        });
    }
}
