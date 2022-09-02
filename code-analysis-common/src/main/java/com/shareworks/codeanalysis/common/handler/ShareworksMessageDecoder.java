package com.shareworks.codeanalysis.common.handler;

import cn.hutool.core.bean.BeanUtil;
import com.shareworks.codeanalysis.common.constant.ExceptionSysConstant;
import com.shareworks.codeanalysis.common.constant.NettyProtocolSysConstant;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.enums.SerializerTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import com.shareworks.codeanalysis.common.handler.serializer.ShareworksSerializer;
import com.shareworks.codeanalysis.common.handler.serializer.ShareworksSerializerFactory;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksProtocolErrorReqDTO;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import lombok.Data;
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
        InputDTO inputDTO = new InputDTO();
        ShareworksMessage<ShareworksBaseDTO> shareworksMessage = parseProtocolHeader(byteBuf, inputDTO);

        // 验证头信息
        if (verifyHeaderInfo(channelHandlerContext, inputDTO, shareworksMessage)) {
            return;
        }

        if (byteBuf.readableBytes() <= NettyProtocolSysConstant.READABLE_BYTE) {
            list.add(shareworksMessage);
            return;
        }

        // 读取消息体长度和数据
        readBodyDetail(byteBuf, shareworksMessage);

        // 签名消息长度及数据
        readSignInfo(byteBuf, shareworksMessage);

        list.add(shareworksMessage);
    }

    /**
     * 读取签名信息
     *
     * @param byteBuf           缓冲字节
     * @param shareworksMessage message
     */
    private void readSignInfo(ByteBuf byteBuf, ShareworksMessage<ShareworksBaseDTO> shareworksMessage) {
        int signLength = byteBuf.readInt();
        byte[] signBytes = new byte[signLength];
        byteBuf.readBytes(signBytes);
        shareworksMessage.setSignature(new String(signBytes, Charset.defaultCharset()));
    }

    /**
     * 读取请求体详情
     *
     * @param byteBuf           缓冲字节
     * @param shareworksMessage message
     */
    private void readBodyDetail(ByteBuf byteBuf, ShareworksMessage<ShareworksBaseDTO> shareworksMessage) {
        int bodyLength = byteBuf.readInt();
        byte[] bodyBytes = new byte[bodyLength];
        byteBuf.readBytes(bodyBytes);
        ShareworksSerializer serializer = ShareworksSerializerFactory.getSerializationType(
                shareworksMessage.getSerializerType());
        ShareworksBaseDTO deserialize = serializer.deserialize(bodyBytes,
                shareworksMessage.getCommandType().getClazz());
        shareworksMessage.setMessageContent(deserialize);
    }

    /**
     * 校验头信息
     *
     * @param channelHandlerContext 上下文
     * @param inputDTO              入参
     * @param shareworksMessage     message
     * @return 校验结果
     */
    private boolean verifyHeaderInfo(ChannelHandlerContext channelHandlerContext, InputDTO inputDTO,
            ShareworksMessage<ShareworksBaseDTO> shareworksMessage) {
        if (NettyProtocolSysConstant.MAGIC_NUMBER != inputDTO.getMagicNumber()) {
            log.error("Invalid magic number , input magicNumber:[{}] , expected magicNumber:[{}] "
                    , inputDTO.getMagicNumber(), NettyProtocolSysConstant.MAGIC_NUMBER);
            writeErrorInfo(channelHandlerContext, shareworksMessage, "Invalid magic number");
            return true;
        }

        if (Objects.isNull(shareworksMessage.getSerializerType())) {
            log.error("Invalid serializer type , input serializerType:[{}]", inputDTO.getSerializerType());
            writeErrorInfo(channelHandlerContext, shareworksMessage, "Invalid serializer type");
            return true;
        }

        if (Objects.isNull(shareworksMessage.getSignType())) {
            log.error("Invalid serializer type , input signType:[{}]", inputDTO.getSignType());
            writeErrorInfo(channelHandlerContext, shareworksMessage, "Invalid sign type");
            return true;
        }

        if (Objects.isNull(shareworksMessage.getCommandType())) {
            log.error("Invalid command type , input commandType:[{}]", inputDTO.getCommandType());
            writeErrorInfo(channelHandlerContext, shareworksMessage, "Invalid command type");
            return true;
        }
        return false;
    }

    /**
     * @param byteBuf 缓冲区
     * @return 信息
     */
    private ShareworksMessage<ShareworksBaseDTO> parseProtocolHeader(ByteBuf byteBuf, InputDTO inputDTO) {
        ShareworksMessage<ShareworksBaseDTO> shareworksMessage = new ShareworksMessage<>();
        // 魔数值
        shareworksMessage.setMagicNumber(byteBuf.readInt());
        // 版本号
        shareworksMessage.setMainVersion(byteBuf.readByte());
        // 序列化类型
        byte serializerType = byteBuf.readByte();
        shareworksMessage.setSerializerType(SerializerTypeEnums.get(serializerType));
        //签名类型
        byte signType = byteBuf.readByte();
        shareworksMessage.setSignType(SignTypeEnums.get(signType));
        // 读取sessionId
        CharSequence sessionId = byteBuf.readCharSequence(18, Charset.defaultCharset());
        shareworksMessage.setSessionId((String) sessionId);
        // 读取当前的消息类型
        byte commandType = byteBuf.readByte();
        shareworksMessage.setCommandType(CommandTypeEnums.get(commandType));

        inputDTO.setMagicNumber(shareworksMessage.getMagicNumber());
        inputDTO.setMainVersion(shareworksMessage.getMainVersion());
        inputDTO.setSerializerType(serializerType);
        inputDTO.setSignType(signType);
        inputDTO.setCommandType(commandType);
        return shareworksMessage;
    }

    /**
     * 写入错误信息
     *
     * @param channelHandlerContext 通道上下文
     * @param shareworksMessage     入参
     */
    private void writeErrorInfo(ChannelHandlerContext channelHandlerContext,
            ShareworksMessage<ShareworksBaseDTO> shareworksMessage, String errorMsg) {
        ShareworksMessage<ShareworksProtocolErrorReqDTO> protocolErrorDTO = new ShareworksMessage<>();
        BeanUtil.copyProperties(shareworksMessage, protocolErrorDTO);
        ShareworksProtocolErrorReqDTO errorReqDTO = new ShareworksProtocolErrorReqDTO();
        errorReqDTO.setErrorCode(ExceptionSysConstant.INTERNAL_SERVER_ERROR);
        errorReqDTO.setErrorMsg(errorMsg);
        protocolErrorDTO.setMessageContent(errorReqDTO);
        channelHandlerContext.writeAndFlush(protocolErrorDTO);
    }

    @Data
    static class InputDTO {

        /**
         * 魔数
         */
        private int magicNumber;
        /**
         * 版本号
         */
        private byte mainVersion;
        /**
         * 序列化类型
         */
        private byte serializerType;
        /**
         * 签名类型
         */
        private byte signType;

        /**
         * 消息类型
         */
        private byte commandType;
    }
}
