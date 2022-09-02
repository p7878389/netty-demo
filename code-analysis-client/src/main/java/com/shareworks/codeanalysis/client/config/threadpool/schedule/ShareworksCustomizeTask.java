package com.shareworks.codeanalysis.client.config.threadpool.schedule;

import com.shareworks.codeanalysis.common.enums.TaskTypeEnums;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 9:58
 */
public interface ShareworksCustomizeTask extends Runnable {

    /**
     * 获取任务类型
     *
     * @return 任务类型
     */
    TaskTypeEnums getTaskType();

    /**
     * 获取netty上下文
     *
     * @return 上下文
     */
    ChannelHandlerContext getContext();
}
