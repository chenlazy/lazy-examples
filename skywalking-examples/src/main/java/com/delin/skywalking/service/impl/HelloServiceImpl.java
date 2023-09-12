package com.delin.skywalking.service.impl;

import com.delin.skywalking.service.HelloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * HelloService
 *
 * @author: chendl
 * @date: Created in 2023/9/8 16:53
 * @description: com.delin.skywalking.service.impl.HelloService
 */
@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    public void helloTrace() {
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        logger.info("invoke HelloService helloTrace method threadId:{} threadName:{}", threadId, threadName);
    }

    @Override
    public void asyncHelloTrace() {
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        logger.info("invoke HelloService asyncHelloTrace method threadId:{} threadName:{}", threadId, threadName);
    }
}
