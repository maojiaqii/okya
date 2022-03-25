package com.top.okya.system.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: maojiaqi
 * @Date: 2020/8/13 16:59
 * @describe： 线程池配置类
 */

@Configuration
@PropertySource(value = "classpath:config.properties", encoding = "utf-8")
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Value("${async.corePoolSize:#{null}}")
    private int asyncCorePoolSize;
    @Value("${async.maxPoolSize:#{null}}")
    private int asyncMaxPoolSize;
    @Value("${async.queueCapacity:#{null}}")
    private int asyncQueueCapacity;
    @Value("${async.threadNamePrefix:#{null}}")
    private String asyncThreadNamePrefix;
    @Value("${async.keepAliveSeconds:#{null}}")
    private int asyncKeepAliveSeconds;

    @Bean
    public Executor asyncServiceExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        //配置核心线程数

        executor.setCorePoolSize(10);

        //配置最大线程数

        executor.setMaxPoolSize(20);

        //配置队列大小

        executor.setQueueCapacity(100);

        //配置线程池中的线程的名称前缀

        executor.setThreadNamePrefix("ssss-");

        // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60);

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务

        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        //执行初始化

        executor.initialize();

        log.info("线程池初始化完成");

        return executor;

    }

}
