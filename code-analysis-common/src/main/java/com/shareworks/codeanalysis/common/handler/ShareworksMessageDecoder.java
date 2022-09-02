package com.shareworks.codeanalysis.common.handler;

import com.shareworks.codeanalysis.common.constant.NettyProtocolSysConstant;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.enums.SerializationTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import com.shareworks.codeanalysis.common.handler.serialization.ShareworksSerializer;
import com.shareworks.codeanalysis.common.handler.serialization.ShareworksSerializerFactory;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.charset.Charset;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 10:35
 */
@Slf4j
@Sharable
public class ShareworksMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        ShareworksMessage<ShareworksBaseDTO> shareworksMessage = new ShareworksMessage<>();
        // 魔数值
        shareworksMessage.setMagicNumber(byteBuf.readInt());
        // 版本号
        shareworksMessage.setMainVersion(byteBuf.readByte());
        // 序列化类型
        shareworksMessage.setSerializationType(SerializationTypeEnums.get(byteBuf.readByte()));
        //签名类型
        shareworksMessage.setSignType(SignTypeEnums.get(byteBuf.readByte()));
        // 读取sessionId
        CharSequence sessionId = byteBuf.readCharSequence(18, Charset.defaultCharset());
        shareworksMessage.setSessionId((String) sessionId);
        // 读取当前的消息类型
        shareworksMessage.setCommandType(CommandTypeEnums.get(byteBuf.readByte()));
        if (byteBuf.readableBytes() <= NettyProtocolSysConstant.READABLE_BYTE) {
            list.add(shareworksMessage);
            return;
        }
        // 读取消息体长度和数据
        int bodyLength = byteBuf.readInt();
        byte[] bodyBytes = new byte[bodyLength];
        byteBuf.readBytes(bodyBytes);

        // 签名消息长度及数据
        int signLength = byteBuf.readInt();
        byte[] signBytes = new byte[signLength];
        byteBuf.readBytes(signBytes);

        ShareworksSerializer serializer = ShareworksSerializerFactory.getSerializationType(
                shareworksMessage.getSerializationType());
        ShareworksBaseDTO deserialize = serializer.deserialize(bodyBytes,
                shareworksMessage.getCommandType().getClazz());
        deserialize.setSignature(new String(signBytes, Charset.defaultCharset()));
        shareworksMessage.setMessageContent(deserialize);
        list.add(shareworksMessage);
    }
}
