package com.lazy.agent.core;

import com.lazy.agent.transformer.LazyMonitorTransformer;

import java.lang.instrument.Instrumentation;

/**
 * @author: delingChen
 * @version: 1.0-SN
 * @date: 2023/6/18 15:40 星期日
 */
public class AgentDemo {

    public static void premain(String agentArgs, Instrumentation instrumentation) {
        System.out.println("hello agent .......");
        instrumentation.addTransformer(new LazyMonitorTransformer());
    }
}
