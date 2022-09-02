package com.shareworks.codeanalysis.client.config.threadpool;

import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 10:22
 */
@Configuration
public class ThreadPoolTaskSchedulerConfig {

    @Resource
    private ThreadPoolProperties threadPoolProperties;

    @Bean
    public ThreadPoolTaskScheduler syncScheduler() {
        ThreadPoolTaskScheduler syncScheduler = new ThreadPoolTaskScheduler();
        syncScheduler.setPoolSize(threadPoolProperties.getPoolSize());
        // 这里给线程设置名字，主要是为了在项目能够更快速的定位错误。
        syncScheduler.setThreadGroupName("code-analysis-client");
        syncScheduler.setThreadNamePrefix("code-analysis-client-thread");
        syncScheduler.setRemoveOnCancelPolicy(true);
        syncScheduler.setAwaitTerminationSeconds(threadPoolProperties.getAwaitTerminationMillis());
        //表明是否等待所有线程执行完任务
        syncScheduler.setWaitForTasksToCompleteOnShutdown(true);
        syncScheduler.initialize();
        return syncScheduler;
    }
}
