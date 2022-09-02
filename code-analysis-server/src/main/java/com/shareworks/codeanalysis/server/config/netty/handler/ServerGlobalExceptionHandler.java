package com.shareworks.codeanalysis.server.config.netty.handler;

import com.shareworks.codeanalysis.common.handler.GlobalExceptionHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/1 10:38
 */
@Slf4j
@Sharable
public class ServerGlobalExceptionHandler extends GlobalExceptionHandler {

    public ServerGlobalExceptionHandler() {
        super(false);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("读数据异常: " + cause);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()) {
                log.error("写数据异常：" + channelFuture.cause());
            }
        }));
    }
}
