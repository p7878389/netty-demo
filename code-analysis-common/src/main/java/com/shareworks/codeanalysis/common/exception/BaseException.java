package com.shareworks.codeanalysis.common.exception;

import lombok.Getter;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 15:43
 */
@Getter
public class BaseException extends RuntimeException {

    private final int errorCode;

    public BaseException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
