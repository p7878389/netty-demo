package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 16:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShareworksAuthRespDTO extends ShareworksBaseDTO {

    private String sessionId;

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.AUTHENTICATION_ACK;
    }
}
