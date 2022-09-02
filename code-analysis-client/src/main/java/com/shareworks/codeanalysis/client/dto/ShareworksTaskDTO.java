package com.shareworks.codeanalysis.client.dto;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 11:52
 */
@Data
@EqualsAndHashCode
public class ShareworksTaskDTO {

    private Long taskId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 表达式
     */
    private String cron;
    /**
     * 立即执行
     */
    private boolean executeImmediately = false;
    /**
     * git commit 列表
     */
    private List<String> commitIdList;

    /**
     * 开始时间
     */
    private Date startDate;

    /**
     * 结束时间
     */
    private Date endDate;

    @EqualsAndHashCode.Exclude
    private ScheduledFuture<?> scheduledFuture;
}
