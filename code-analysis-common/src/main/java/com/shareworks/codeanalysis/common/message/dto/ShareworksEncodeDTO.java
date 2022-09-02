package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.SerializerTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Data;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 9:25
 */
@Data
@Builder
public class ShareworksEncodeDTO {

    ByteBuf out;
    /**
     * 版本号
     */
    byte version;
    String sessionId;
    SerializerTypeEnums serializationType;
    /**
     * 签名类型
     */
    SignTypeEnums signType;
    /**
     * 签名key
     */
    private String signKey;
}
