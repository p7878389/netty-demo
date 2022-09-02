package com.shareworks.codeanalysis.common.functional;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 10:07
 */
@FunctionalInterface
public interface SignTypeStrategy<T, K> {

    T apply(K data, String signKey);
}
