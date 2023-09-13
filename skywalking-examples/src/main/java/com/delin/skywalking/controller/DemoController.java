package com.delin.skywalking.controller;

import com.delin.skywalking.service.DemoService;
import com.delin.skywalking.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.CallableWrapper;
import org.apache.skywalking.apm.toolkit.trace.ConsumerWrapper;
import org.apache.skywalking.apm.toolkit.trace.FunctionWrapper;
import org.apache.skywalking.apm.toolkit.trace.SupplierWrapper;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.slf4j.MDC;
import lombok.RequiredArgsConstructor;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
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

    private Map<String, String> map;

    @GetMapping("traceIdPrint")
    public void testTraceIdPrint() {
        demoService.traceDemo();
        helloService.helloTrace();
    }

    @GetMapping("/mdc")
    public void testMdc() {
        log.info("====MDC demo====");
        Executors.newSingleThreadExecutor().execute(() -> {
            MDC.put("TEST-MDC", "MDC-A");
            log.info("test mdc save threadId:{}", Thread.currentThread().getId());
        });
        Executors.newSingleThreadExecutor().execute(() -> {
            MDC.put("TEST-MDC", "MDC-B");
            log.info("test mdc save data threadId:{}", Thread.currentThread().getId());
        });
    }

    @GetMapping("testAsyncTraceIdDropPrint")
    public void testAsyncTraceIdDropPrint() {
        demoService.traceDemo();
        helloService.asyncHelloTrace();
    }

    @GetMapping("testAsyncTraceIdPrint")
    public String testAsyncTraceIdPrint() {
        demoService.traceDemo();
        CompletableFuture.supplyAsync(new SupplierWrapper<>(() -> {
            log.info("test CallableWrapper demo");
            return "CallableWrapper";
        })).thenApply(new FunctionWrapper<>((String str) -> {
            log.info("test FunctionWrapper demo str:{}", str);
            return "FunctionWrapper";
        })).thenAccept(new ConsumerWrapper<>((String str) -> {
            log.info("test ConsumerWrapper demo str:{}", str);
        }));
        Executors.newSingleThreadExecutor().execute(new RunnableWrapper(helloService::asyncHelloTrace));
        Executors.newSingleThreadExecutor().submit(new CallableWrapper<>(() -> {
            log.info("test CallableWrapper demo");
            return "test CallableWrapper";
        }));
        return TraceContext.traceId();
    }

    @GetMapping("traceIdDropRepetition")
    public void traceIdDropRepetition() {
        helloService.traceIdDropRepetition();
    }

    @GetMapping("/mdcCopy")
    public void testMdcCopy() {
        log.info("====test MdcCopy demo====");
        Executors.newSingleThreadExecutor().execute(() -> {
            MDC.put("TEST-MDC", "MDC-A");
            log.info("test mdc save threadId:{}", Thread.currentThread().getId());
        });
        Executors.newSingleThreadExecutor().execute(() -> {
            MDC.put("TEST-MDC", "MDC-B");
            log.info("test mdc save data threadId:{}", Thread.currentThread().getId());
        });
    }
}
