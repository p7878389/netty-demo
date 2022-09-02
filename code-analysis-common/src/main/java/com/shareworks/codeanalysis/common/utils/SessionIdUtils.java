package com.shareworks.codeanalysis.common.utils;

import cn.hutool.core.util.RandomUtil;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 15:17
 */
public class SessionIdUtils {

    public static String generateId() {
        return RandomUtil.randomNumbers(18);
    }

    public static String generateTraceId() {
        return RandomUtil.randomNumbers(18);
    }
}
