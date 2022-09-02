package com.shareworks.codeanalysis.client.config.netty.handler;

import com.shareworks.codeanalysis.client.config.netty.NettyClientHolder;
import com.shareworks.codeanalysis.client.config.netty.NettyClientProperties;
import com.shareworks.codeanalysis.client.utils.NettyUtils;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksHeartbeatReqDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.Set;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 17:16
 */
@Component
@Slf4j
public class HeartBeatRequestHandler extends ChannelInboundHandlerAdapter {

    @Resource
    private NettyClientProperties nettyClientProperties;

    private static final Set<IdleState> IDLE_STATE_SET = Set.of(IdleState.READER_IDLE, IdleState.WRITER_IDLE,
            IdleState.ALL_IDLE);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ShareworksMessage<ShareworksBaseDTO> message = (ShareworksMessage<ShareworksBaseDTO>) msg;
        if (CommandTypeEnums.PING_ACK.equals(message.getCommandType())) {
            log.debug("received ping packet");
            ctx.fireChannelActive();
            return;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }
        IdleStateEvent event = (IdleStateEvent) evt;
        IdleState idleState = event.state();
        if (!IDLE_STATE_SET.contains(idleState)) {
            return;
        }
        ShareworksMessage<ShareworksHeartbeatReqDTO> shareworksMessage = NettyUtils.getSendMessage(
                nettyClientProperties,
                new ShareworksHeartbeatReqDTO());
        shareworksMessage.setSessionId(NettyClientHolder.getSessionId());
        ctx.writeAndFlush(shareworksMessage);
    }
}
