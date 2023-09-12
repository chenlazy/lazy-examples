package com.delin.skywalking.service;

import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * DemoService
 *
 * @author: chendl
 * @date: Created in 2023/9/8 16:54
 * @description: com.delin.skywalking.service.impl.DemoService
 */
@SpringBootTest
class DemoServiceTest {

    @Autowired
    private DemoService demoService;

    @Autowired
    private HelloService helloService;

    @Test
    void testTraceId() {
        demoService.traceDemo();
        helloService.helloTrace();
    }
}
