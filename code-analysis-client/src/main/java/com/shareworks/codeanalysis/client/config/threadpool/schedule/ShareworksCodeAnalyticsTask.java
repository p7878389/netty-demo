package com.shareworks.codeanalysis.client.config.threadpool.schedule;

import com.shareworks.codeanalysis.common.enums.TaskTypeEnums;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 10:11
 */
@AllArgsConstructor
public class ShareworksCodeAnalyticsTask implements ShareworksCustomizeTask {

    private final ChannelHandlerContext channelHandlerContext;

    @Override
    public TaskTypeEnums getTaskType() {
        return TaskTypeEnums.CODE_ANALYTICS;
    }

    @Override
    public ChannelHandlerContext getContext() {
        return channelHandlerContext;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        System.out.println("Starting code analysis...");
    }
}
