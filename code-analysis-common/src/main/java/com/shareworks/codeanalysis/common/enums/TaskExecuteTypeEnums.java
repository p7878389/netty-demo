package com.shareworks.codeanalysis.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/2 10:03
 */
@AllArgsConstructor
@Getter
public enum TaskExecuteTypeEnums implements BaseEnums {
    /**
     * cron 表达式执行
     */
    CRON((byte) 0),
    /**
     * 立即执行
     */
    IMMEDIATELY((byte) 1),
    ;

    private final byte type;

    private final String name = name();

}
