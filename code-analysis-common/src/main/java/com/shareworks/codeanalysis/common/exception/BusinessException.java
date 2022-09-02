package com.shareworks.codeanalysis.common.exception;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:55
 */
public class BusinessException extends BaseException{

    public BusinessException(int errorCode, String message) {
        super(errorCode, message);
    }

    public BusinessException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
