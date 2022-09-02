package com.shareworks.codeanalysis.server.config.netty.handler;

import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 15:11
 */
@Component("ServerBusinessHandler")
@Sharable
public class ServerBusinessHandler extends SimpleChannelInboundHandler<ShareworksMessage<ShareworksBaseDTO>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ShareworksMessage<ShareworksBaseDTO> msg) throws Exception {

    }
}
