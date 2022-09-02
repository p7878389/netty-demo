package com.shareworks.codeanalysis.client.config.netty.handler;

import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:39
 */
@Component
public class BusinessHandler extends SimpleChannelInboundHandler<ShareworksMessage<ShareworksBaseDTO>> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ShareworksMessage<ShareworksBaseDTO> msg) {

    }
}
