package com.shareworks.codeanalysis.client.functional;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 11:12
 */
@FunctionalInterface
public interface CommandTaskStrategy<T> {

    T commandTask();
}
