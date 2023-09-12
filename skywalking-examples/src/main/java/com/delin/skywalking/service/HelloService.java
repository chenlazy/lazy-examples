package com.delin.skywalking.service;

/**
 * HelloService
 *
 * @author: chendl
 * @date: Created in 2023/9/8 17:06
 * @description: com.delin.skywalking.service.HelloService
 */
public interface HelloService {

    void helloTrace();

    void asyncHelloTrace();

    void traceIdDropRepetition();
}
