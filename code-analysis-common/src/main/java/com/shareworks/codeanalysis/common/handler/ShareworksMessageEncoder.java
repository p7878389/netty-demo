package com.shareworks.codeanalysis.common.handler;

import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksEncodeDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 15:53
 */
@Sharable
public class ShareworksMessageEncoder extends MessageToByteEncoder<ShareworksMessage<ShareworksBaseDTO>> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ShareworksMessage<ShareworksBaseDTO> shareworksMessage,
            ByteBuf byteBuf) {
        ShareworksEncodeDTO shareworksEncodeDTO = ShareworksEncodeDTO.builder()
                .serializationType(shareworksMessage.getSerializerType())
                .out(byteBuf)
                .version(shareworksMessage.getMainVersion())
                .sessionId(shareworksMessage.getSessionId())
                .signType(shareworksMessage.getSignType())
                .signKey(shareworksMessage.getSignKey())
                .build();
        shareworksMessage.getMessageContent().encode(shareworksEncodeDTO);
        ctx.writeAndFlush(byteBuf);
    }
}
