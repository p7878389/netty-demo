package com.shareworks.codeanalysis.client.config.threadpool.schedule;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import javax.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 11:00
 */
@Component
@Slf4j
@AllArgsConstructor
public class DynamicScheduleTask {

    private static final Map<String, ScheduledFuture<?>> TASK_CONTAINER_MAP = new ConcurrentHashMap<>();

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    /**
     * 添加定时任务
     *
     * @param scheduleDTO 入参
     */
    public void addCronTask(ScheduleDTO scheduleDTO) {
        ScheduledFuture<?> schedule = threadPoolTaskScheduler.schedule(scheduleDTO.getRunnable()
                , new CronTrigger(scheduleDTO.getCron()));
        TASK_CONTAINER_MAP.put(scheduleDTO.getTaskId(), schedule);
    }

    /**
     * 停止任务
     *
     * @param taskId 任务ID
     */
    public void stopTask(String taskId) {
        ScheduledFuture<?> scheduledFuture = TASK_CONTAINER_MAP.get(taskId);
        if (Objects.isNull(scheduledFuture)) {
            return;
        }
        scheduledFuture.cancel(false);
        TASK_CONTAINER_MAP.remove(taskId);
    }

    /**
     * 更新任务
     *
     * @param scheduleDTO 入参
     */
    public void updateTaskCron(ScheduleDTO scheduleDTO) {
        ScheduledFuture<?> scheduledFuture = TASK_CONTAINER_MAP.get(scheduleDTO.getTaskId());
        if (Objects.isNull(scheduledFuture)) {
            addCronTask(scheduleDTO);
            return;
        }
        stopTask(scheduleDTO.getTaskId());
        addCronTask(scheduleDTO);
    }

    /**
     * 立即执行
     *
     * @param runnable 任务
     */
    public void runNow(Runnable runnable) {
        threadPoolTaskScheduler.submit(runnable);
    }
}
