package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 16:38
 */
public class ShareworksHeartbeatReqDTO extends ShareworksBaseDTO{

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.PING;
    }
}
