package com.shareworks.codeanalysis.common.message;

import com.shareworks.codeanalysis.common.constant.NettyProtocolSysConstant;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import com.shareworks.codeanalysis.common.handler.serialization.ShareworksSerializer;
import com.shareworks.codeanalysis.common.handler.serialization.ShareworksSerializerFactory;
import com.shareworks.codeanalysis.common.message.dto.ShareworksEncodeDTO;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;
import org.apache.commons.lang3.StringUtils;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 17:05
 */
@SuppressWarnings("JavadocDeclaration")
public interface ShareworksMessageConvert {

    /**
     * 消息编码器
     *
     * @param encodeDTO 编码数据
     */
    default void encode(ShareworksEncodeDTO encodeDTO) {
        ByteBuf out = encodeDTO.getOut();
        writeProtocolHeader(encodeDTO, out);

        byte[] bodyBytes = writeBodyInfo(encodeDTO, out);

        dataSignature(out, encodeDTO.getSignType(), bodyBytes, encodeDTO.getSignKey());
    }

    /**
     * 写入请求详情
     *
     * @param encodeDTO
     * @param out
     * @return
     */
    private byte[] writeBodyInfo(ShareworksEncodeDTO encodeDTO, ByteBuf out) {
        // 具体消息
        byte[] bodyBytes = serializationBody(encodeDTO);
        if (bodyBytes.length == 0) {
            out.writeInt(0);
        } else {
            out.writeInt(bodyBytes.length);
            out.writeBytes(bodyBytes);
        }
        return bodyBytes;
    }

    /**
     * 写入协议头信息
     *
     * @param encodeDTO
     * @param out
     */
    private void writeProtocolHeader(ShareworksEncodeDTO encodeDTO, ByteBuf out) {
        // 写入当前的魔数
        out.writeInt(NettyProtocolSysConstant.MAGIC_NUMBER);
        // 写入当前的主版本号
        out.writeByte(encodeDTO.getVersion());
        //序列化类型
        out.writeByte(encodeDTO.getSerializationType().getType());
        //序列化类型
        out.writeByte(encodeDTO.getSignType().getType());
        // sessionId
        String sessionId = encodeDTO.getSessionId();
        if (StringUtils.isBlank(sessionId)) {
            sessionId = SessionIdUtils.generateId();
        }
        out.writeCharSequence(sessionId, Charset.defaultCharset());
        // 消息类型
        out.writeByte(getCommandType().getType());
    }

    /**
     * 获取指令类型
     *
     * @return 指令类型
     */
    CommandTypeEnums getCommandType();

    /**
     * 序列化消息
     *
     * @param encodeDTO 入参
     * @return byte[]
     */
    default byte[] serializationBody(ShareworksEncodeDTO encodeDTO) {
        ShareworksSerializer shareworksSerializer = ShareworksSerializerFactory.getSerializationType(
                encodeDTO.getSerializationType());
        return shareworksSerializer.serialize(this);
    }


    /**
     * 数据签名
     *
     * @param out
     * @param signType
     * @param bodyBytes
     * @param signKey
     * @return
     */
    default void dataSignature(ByteBuf out, SignTypeEnums signType, byte[] bodyBytes, String signKey) {
        byte[] signBytes = signType.getSignTypeStrategy().apply(bodyBytes, signKey);
        out.writeInt(signBytes.length);
        out.writeBytes(signBytes);
    }
}
