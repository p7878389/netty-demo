package com.shareworks.codeanalysis.client.config.threadpool;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 10:23
 */
@ConfigurationProperties(prefix = "spring.thread-pool")
@Component
@Data
public class ThreadPoolProperties {

    private int poolSize;

    private int awaitTerminationMillis;
}
