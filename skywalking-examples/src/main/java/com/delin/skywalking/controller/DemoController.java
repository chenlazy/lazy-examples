package com.delin.skywalking.controller;

import com.delin.skywalking.service.DemoService;
import com.delin.skywalking.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import lombok.RequiredArgsConstructor;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Executors;


/**
 * DemoController
 *
 * @author: chendl
 * @date: Created in 2023/9/8 17:10
 * @description: com.delin.skywalking.controller.DemoController
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class DemoController {

    private final DemoService demoService;

    private final HelloService helloService;



    @GetMapping("traceIdPrint")
    public void testTraceIdPrint() {
        demoService.traceDemo();
        helloService.helloTrace();
    }

    @GetMapping("/mdc")
    public void testMdc() {
        System.out.println("====MDC demo====");
        Executors.newSingleThreadExecutor().execute(() -> {
            MDC.put("MDC", "MDC-A");
            log.info("test mdc save threadId:{}", Thread.currentThread().getId());
        });
        Executors.newSingleThreadExecutor().execute(() -> {
            MDC.put("MDC", "MDC-B");
            log.info("test mdc save data threadId:{}", Thread.currentThread().getId());
        });
    }

    @GetMapping("testAsyncTraceIdDropPrint")
    public void testAsyncTraceIdDropPrint() {
        demoService.traceDemo();
        Executors.newSingleThreadExecutor().execute(helloService::asyncHelloTrace);
    }

    @GetMapping("testAsyncTraceIdPrint")
    public void testAsyncTraceIdPrint() {
        demoService.traceDemo();
        Executors.newSingleThreadExecutor().execute(new RunnableWrapper(helloService::asyncHelloTrace));
    }
}
