package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 16:55
 */
public class ShareworksProtocolErrorReqDTO extends ShareworksBaseDTO{

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.PROTOCOL_ERROR;
    }
}
