package com.shareworks.codeanalysis.server.config.netty.handler;

import com.shareworks.codeanalysis.common.constant.ExceptionSysConstant;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksAuthReqDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksAuthRespDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import com.shareworks.codeanalysis.server.config.netty.NettyChannelDTO;
import com.shareworks.codeanalysis.server.config.netty.NettySocketHolder;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:40
 */
@Component("AuthorizationResponseHandler")
@Sharable
@Slf4j
public class AuthorizationResponseHandler extends SimpleChannelInboundHandler<ShareworksMessage<ShareworksBaseDTO>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ShareworksMessage<ShareworksBaseDTO> msg) {
        ShareworksBaseDTO shareworksBaseDTO = msg.getMessageContent();
        if (!CommandTypeEnums.AUTHENTICATION.equals(msg.getCommandType())) {
            NettyChannelDTO nettyChannelDTO = NettySocketHolder.get(ctx.channel());
            if (Objects.nonNull(nettyChannelDTO)) {
                ctx.fireChannelRead(msg);
                return;
            }
            ShareworksAuthRespDTO authRespDTO = new ShareworksAuthRespDTO();
            authRespDTO.setErrorCode(ExceptionSysConstant.INTERNAL_SERVER_ERROR);
            authRespDTO.setErrorMsg("连接未认证");
            msg.setMessageContent(authRespDTO);
            ctx.fireChannelRead(msg);
            ctx.close();
            if (log.isDebugEnabled()) {
                log.debug("connect unauthenticated , prepare automatically disconnect");
            }
            return;
        }

        if (Objects.isNull(shareworksBaseDTO)) {
            msg.setCommandType(CommandTypeEnums.AUTHENTICATION_ACK);
            ShareworksAuthRespDTO authRespDTO = new ShareworksAuthRespDTO();
            authRespDTO.setErrorCode(ExceptionSysConstant.INTERNAL_SERVER_ERROR);
            authRespDTO.setErrorMsg("认证失败，请求详情为空");
            msg.setMessageContent(authRespDTO);
            ctx.writeAndFlush(msg);
            if (log.isDebugEnabled()) {
                log.debug("认证失败，请求详情为空，自动断开连接");
                log.debug("connect unauthenticated , request body is null, prepare automatically disconnect");
            }
            ctx.close();
            return;
        }

        ShareworksAuthReqDTO authReqDTO = (ShareworksAuthReqDTO) shareworksBaseDTO;
        if (StringUtils.isBlank(authReqDTO.getAppSecret()) || StringUtils.isBlank(authReqDTO.getAppKey())) {
            ShareworksAuthRespDTO authRespDTO = new ShareworksAuthRespDTO();
            authRespDTO.setErrorCode(ExceptionSysConstant.INTERNAL_SERVER_ERROR);
            authRespDTO.setErrorMsg("认证失败，appKey或appSecret不能为空");
            msg.setMessageContent(authRespDTO);
            ctx.writeAndFlush(msg);
            if (log.isDebugEnabled()) {
                log.debug("authentication failed , appKey or appSecret is required , prepare automatically disconnect");
            }
            ctx.close();
            return;
        }

        msg.setCommandType(CommandTypeEnums.AUTHENTICATION_ACK);
        ShareworksAuthRespDTO authRespDTO = new ShareworksAuthRespDTO();
        authRespDTO.setErrorCode(ExceptionSysConstant.SUCCESS);
        authRespDTO.setSessionId(SessionIdUtils.generateId());
        authRespDTO.setTraceId(authReqDTO.getTraceId());
        msg.setMessageContent(authRespDTO);

        NettyChannelDTO channelDTO = NettyChannelDTO.builder()
                .channel(ctx.channel())
                .appKey(authReqDTO.getAppKey())
                .sessionId(authRespDTO.getSessionId())
                .signType(msg.getSignType())
                .build();
        NettySocketHolder.put(ctx.channel(), channelDTO);
        if (log.isDebugEnabled()) {
            log.debug("authentication successful appKey:[{}] ， sessionId:[{}]", authReqDTO.getAppKey(),
                    authRespDTO.getSessionId());
        }
        // 认证成功
        ctx.writeAndFlush(msg);
    }


}
