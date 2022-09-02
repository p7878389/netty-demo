package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 11:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShareworksPullTaskRespDTO extends ShareworksBaseDTO {

    private List<CodeAnalysisTaskDTO> taskDTOList;

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.PULL_TASK_ACK;
    }

    @Data
    static class CodeAnalysisTaskDTO {

        private Long taskId;

        /**
         * 项目ID
         */
        private Long projectId;

        /**
         * 表达式
         */
        private String cron;
        /**
         * 立即执行
         */
        private boolean executeImmediately = false;
        /**
         * git commit 列表
         */
        private List<String> commitIdList;

        /**
         * 开始时间
         */
        private Date startDate;

        /**
         * 结束时间
         */
        private Date endDate;
    }
}
