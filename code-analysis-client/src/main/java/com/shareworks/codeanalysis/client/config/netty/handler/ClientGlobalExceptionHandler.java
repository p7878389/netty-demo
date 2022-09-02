package com.shareworks.codeanalysis.client.config.netty.handler;

import com.shareworks.codeanalysis.common.handler.GlobalExceptionHandler;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/1 10:38
 */
@Slf4j
@Component
public class ClientGlobalExceptionHandler extends GlobalExceptionHandler {

    public ClientGlobalExceptionHandler() {
        super(true);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        super.write(ctx, msg, promise);
    }

    @Override
    public void businessProcess(ShareworksMessage<ShareworksBaseDTO> message) {

    }
}
