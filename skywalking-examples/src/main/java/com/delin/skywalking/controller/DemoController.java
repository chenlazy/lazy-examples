package com.delin.skywalking.controller;

import com.delin.skywalking.service.DemoService;
import com.delin.skywalking.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController
 *
 * @author: chendl
 * @date: Created in 2023/9/8 17:10
 * @description: com.delin.skywalking.controller.DemoController
 */
@RestController
public class DemoController {

    private final DemoService demoService;

    private final HelloService helloService;


    public DemoController(DemoService demoService, HelloService helloService) {
        this.demoService = demoService;
        this.helloService = helloService;
    }

    @GetMapping("traceIdPrint")
    public void testTraceIdPrint() {
        demoService.traceDemo();
        helloService.helloTrace();
    }
}
