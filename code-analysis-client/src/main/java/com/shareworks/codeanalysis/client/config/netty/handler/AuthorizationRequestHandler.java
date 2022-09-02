package com.shareworks.codeanalysis.client.config.netty.handler;

import com.shareworks.codeanalysis.client.config.netty.NettyClientHolder;
import com.shareworks.codeanalysis.client.config.netty.NettyClientProperties;
import com.shareworks.codeanalysis.client.config.threadpool.schedule.DynamicScheduleTask;
import com.shareworks.codeanalysis.client.config.threadpool.schedule.ScheduleDTO;
import com.shareworks.codeanalysis.client.config.threadpool.schedule.ShareworksCodeAnalyticsTask;
import com.shareworks.codeanalysis.client.utils.NettyUtils;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksAuthReqDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksAuthRespDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:40
 */
@Component("AuthorizationRequestHandler")
@Sharable
@Slf4j
public class AuthorizationRequestHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private NettyClientProperties nettyClientProperties;

    @Resource
    private DynamicScheduleTask dynamicScheduleTask;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ShareworksAuthReqDTO authReqDTO = new ShareworksAuthReqDTO();
        authReqDTO.setAppKey(nettyClientProperties.getAppKey());
        authReqDTO.setAppSecret(nettyClientProperties.getAppSecret());
        authReqDTO.setTraceId(SessionIdUtils.generateTraceId());

        ShareworksMessage<ShareworksAuthReqDTO> shareworksMessage = NettyUtils.getSendMessage(nettyClientProperties,
                authReqDTO);
        // 连接成功后发起认证请求
        ctx.writeAndFlush(shareworksMessage);

        ScheduleDTO scheduleDTO = ScheduleDTO.builder()
                .cron("0/5 * * * * ?")
                .taskId("0/5 * * * * ?")
                .runnable(new ShareworksCodeAnalyticsTask(ctx))
                .build();
        dynamicScheduleTask.addCronTask(scheduleDTO);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ShareworksMessage<ShareworksBaseDTO> message = (ShareworksMessage<ShareworksBaseDTO>) msg;
        ShareworksBaseDTO shareworksBaseDTO = message.getMessageContent();
        if (!CommandTypeEnums.AUTHENTICATION_ACK.equals(message.getCommandType())) {
            ctx.fireChannelRead(msg);
            return;
        }

        if (Objects.isNull(shareworksBaseDTO)) {
            ctx.channel().close();
            log.error("authentication failed , response is null , preparing automatic exit");
            System.exit(-1);
            return;
        }
        ShareworksAuthRespDTO authRespDTO = (ShareworksAuthRespDTO) shareworksBaseDTO;
        if (!authRespDTO.isSucceeded()) {
            log.error("authentication failed , errorCode:[{}] , errorMsg:[{}] , preparing automatic exit",
                    authRespDTO.getErrorCode(),
                    authRespDTO.getErrorMsg());
            ctx.channel().close();
            System.exit(-1);
            return;
        }
        log.debug("authentication successfully , sessionId:[{}]", authRespDTO.getSessionId());
        String sessionId = authRespDTO.getSessionId();
        NettyClientHolder.setSessionId(sessionId);
        NettyClientHolder.setContext(ctx);
        ctx.fireChannelActive();
    }
}
