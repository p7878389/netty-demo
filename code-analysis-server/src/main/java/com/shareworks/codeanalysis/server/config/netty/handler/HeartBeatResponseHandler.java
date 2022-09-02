package com.shareworks.codeanalysis.server.config.netty.handler;

import cn.hutool.core.util.RandomUtil;
import com.shareworks.codeanalysis.common.constant.ExceptionSysConstant;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.enums.SerializationTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksHeartbeatReqDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksHeartbeatRespDTO;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import com.shareworks.codeanalysis.server.config.netty.NettyChannelDTO;
import com.shareworks.codeanalysis.server.config.netty.NettySocketHolder;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 14:56
 */
@Component("HeartBeatResponseHandler")
@Sharable
@Slf4j
public class HeartBeatResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        NettySocketHolder.remove(ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ShareworksMessage<ShareworksBaseDTO> message = (ShareworksMessage<ShareworksBaseDTO>) msg;
        if (!CommandTypeEnums.PING.equals(message.getCommandType())) {
            ctx.fireChannelRead(msg);
            return;
        }
        ShareworksBaseDTO messageContent = message.getMessageContent();

        ShareworksHeartbeatRespDTO heartbeatRespDTO = new ShareworksHeartbeatRespDTO();

        if (Objects.isNull(messageContent)) {
            heartbeatRespDTO.setErrorCode(ExceptionSysConstant.INTERNAL_SERVER_ERROR);
            heartbeatRespDTO.setErrorMsg("心跳检测失败，详情不存在");
            message.setMessageContent(heartbeatRespDTO);
            ctx.writeAndFlush(message);
            return;
        }

        heartbeatRespDTO.setErrorCode(ExceptionSysConstant.SUCCESS);
        heartbeatRespDTO.setTraceId(messageContent.getTraceId());
        message.setMessageContent(heartbeatRespDTO);
        ctx.writeAndFlush(message);
        ctx.fireChannelActive();
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            idleStateProcess(ctx, (IdleStateEvent) evt);
        }
        super.userEventTriggered(ctx, evt);
    }

    private void idleStateProcess(ChannelHandlerContext ctx, IdleStateEvent evt) {
        if (evt.state() == IdleState.READER_IDLE) {
            if (log.isDebugEnabled()) {
                log.info("已经30秒没有收到信息！");
            }
            NettyChannelDTO nettyChannelDTO = NettySocketHolder.get(ctx.channel());
            if (Objects.isNull(nettyChannelDTO)) {
//                log.warn("connect unauthenticated ，netty channel initiative close");
//                ctx.close();
//                return;
                nettyChannelDTO = NettyChannelDTO.builder()
                        .sessionId(SessionIdUtils.generateId())
                        .serializationType(SerializationTypeEnums.JSON)
                        .signType(SignTypeEnums.MD5)
                        .version((byte) 1)
                        .build();
            }
            ShareworksMessage<ShareworksHeartbeatReqDTO> message = new ShareworksMessage<>();
            message.setSessionId(nettyChannelDTO.getSessionId());
            message.setMessageContent(new ShareworksHeartbeatReqDTO());
            message.setCommandType(CommandTypeEnums.PONG);
            message.setSerializationType(nettyChannelDTO.getSerializationType());
            message.setMainVersion(nettyChannelDTO.getVersion());
            message.setSignType(nettyChannelDTO.getSignType());
            message.setMagicNumber(RandomUtil.randomInt(4));
            //向客户端发送消息
            ctx.writeAndFlush(message).addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    NettySocketHolder.remove(future.channel());
//                    future.channel().close();
                }
            });
        }
    }
}
