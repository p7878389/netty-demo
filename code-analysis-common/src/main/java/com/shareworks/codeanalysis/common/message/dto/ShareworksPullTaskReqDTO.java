package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 11:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShareworksPullTaskReqDTO extends ShareworksBaseDTO {

    /**
     * 项目ID
     */
    private List<Long> projectIdList;

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.PULL_TASK;
    }
}
