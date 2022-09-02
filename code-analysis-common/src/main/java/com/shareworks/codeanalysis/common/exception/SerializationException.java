package com.shareworks.codeanalysis.common.exception;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 9:39
 */
public class SerializationException extends BaseException {

    public SerializationException(int errorCode, String message) {
        super(errorCode, message);
    }

    public SerializationException(int errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
