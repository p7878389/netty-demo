package com.shareworks.codeanalysis.common.exception;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 10:14
 */
public class SignTypeException extends BaseException{

    public SignTypeException(int errorCode, String message) {
        super(errorCode, message);
    }

    public SignTypeException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
