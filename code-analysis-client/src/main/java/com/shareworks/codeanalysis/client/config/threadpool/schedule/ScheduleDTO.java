package com.shareworks.codeanalysis.client.config.threadpool.schedule;

import lombok.Builder;
import lombok.Data;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 9:53
 */
@Data
@Builder
public class ScheduleDTO {

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 具体任务
     */
    private Runnable runnable;

    /**
     * 表达式
     */
    private String cron;
}
