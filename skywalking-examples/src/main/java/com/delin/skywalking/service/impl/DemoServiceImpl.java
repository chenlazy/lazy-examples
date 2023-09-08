package com.delin.skywalking.service.impl;

import com.delin.skywalking.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * DemoService
 *
 * @author: chendl
 * @date: Created in 2023/9/8 16:54
 * @description: com.delin.skywalking.service.impl.DemoService
 */
@Service
public class DemoServiceImpl implements DemoService {

    private static final Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    public void traceDemo() {
        long threadId = Thread.currentThread().getId();
        String threadName = Thread.currentThread().getName();
        logger.info("invoke DemoService traceDemo method threadId:{} threadName:{}", threadId, threadName);
    }
}
