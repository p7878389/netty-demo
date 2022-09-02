package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 15:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShareworksHeartbeatRespDTO extends ShareworksBaseDTO {

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.PING_ACK;
    }
}
