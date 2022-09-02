package com.shareworks.codeanalysis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 10:01
 */
@AllArgsConstructor
@Getter
public enum TaskTypeEnums implements BaseEnums {

    /**
     * 代码分析
     */
    CODE_ANALYTICS((byte) 0),
    /**
     * jira分析
     */
    JIRA_ANALYTICS((byte) 1),
    ;

    private final byte type;

    private final String name = name();
}
