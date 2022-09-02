package com.shareworks.codeanalysis.common.message;

import com.shareworks.codeanalysis.common.enums.SerializationTypeEnums;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import lombok.Data;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 15:34
 */
@Data
public class ShareworksMessage<T extends ShareworksBaseDTO> {

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
    private SerializationTypeEnums serializationType;
    /**
     * 签名类型
     */
    private SignTypeEnums signType;
    /**
     * 签名类型
     */
    private String signKey;
    /**
     * sessionId
     */
    private String sessionId;
    /**
     * 消息类型
     */
    private CommandTypeEnums commandType;
    /**
     * 消息长度
     */
    private int length;
    /**
     * 消息内容
     */
    private T messageContent;
}
