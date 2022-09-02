package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 16:56
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShareworksProtocolErrorRespDTO extends ShareworksBaseDTO {

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.PROTOCOL_ERROR_ACK;
    }
}
