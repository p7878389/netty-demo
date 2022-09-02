package com.shareworks.codeanalysis.client.config.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:39
 */
@Component
public class BusinessHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ShareworksMessage<? extends ShareworksBaseDTO> shareworksMessage = (ShareworksMessage<? extends ShareworksBaseDTO>) msg;
//        ShareworksCommandBusiness<? extends ShareworksBaseDTO> shareworksBusiness = ShareworksBusinessCommandFactory.getShareworksBusiness(
//                shareworksMessage.getCommandType());
//        shareworksBusiness.processCommand(shareworksMessage);
    }
}
