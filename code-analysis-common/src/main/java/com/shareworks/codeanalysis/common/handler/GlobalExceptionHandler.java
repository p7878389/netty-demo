package com.shareworks.codeanalysis.common.handler;

import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/1 10:33
 */
@Slf4j
public class GlobalExceptionHandler extends ChannelDuplexHandler {

    private final boolean client;

    public GlobalExceptionHandler(boolean client) {
        this.client = client;
    }

    public boolean isClient() {
        return client;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("read data failed", cause);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.write(msg, promise.addListener((ChannelFutureListener) channelFuture -> {
            if (!channelFuture.isSuccess()) {
                log.error("write failed", channelFuture.cause());
                if (!(msg instanceof ShareworksMessage)) {
                    return;
                }
                businessProcess((ShareworksMessage<ShareworksBaseDTO>) msg);
            }
        }));
    }

    /**
     * 写入消息失败后处理
     *
     * @param message 消息
     */
    public void businessProcess(ShareworksMessage<ShareworksBaseDTO> message) {
    }
}
