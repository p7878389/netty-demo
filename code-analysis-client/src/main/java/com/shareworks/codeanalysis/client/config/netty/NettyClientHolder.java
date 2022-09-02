package com.shareworks.codeanalysis.client.config.netty;

import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:09
 */
public final class NettyClientHolder {

    private static volatile String SESSION_ID;

    private static volatile ChannelHandlerContext context;

    private static volatile boolean START_FLAG;

    private static final AtomicInteger RETRY_COUNT = new AtomicInteger(0);

    public static String getSessionId() {
        return SESSION_ID;
    }

    public static void setSessionId(String sessionId) {
        SESSION_ID = sessionId;
    }

    public static ChannelHandlerContext getContext() {
        return context;
    }

    public synchronized static void setContext(ChannelHandlerContext context) {
        NettyClientHolder.context = context;
    }

    public static boolean isStartFlag() {
        return START_FLAG;
    }

    public static void setStartFlag(boolean startFlag) {
        START_FLAG = startFlag;
    }

    public static void retryCountInc() {
        RETRY_COUNT.incrementAndGet();
    }

    public static void clearRetryCount() {
        RETRY_COUNT.set(0);
    }

    public static boolean isExceedThreshold(int retryThreshold) {
        return RETRY_COUNT.get() > retryThreshold;
    }
}
