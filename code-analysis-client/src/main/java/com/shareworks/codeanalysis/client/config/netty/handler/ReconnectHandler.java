package com.shareworks.codeanalysis.client.config.netty.handler;

import com.shareworks.codeanalysis.client.config.SpringBeanHolder;
import com.shareworks.codeanalysis.client.config.netty.NettyClient;
import com.shareworks.codeanalysis.client.config.netty.NettyClientHolder;
import com.shareworks.codeanalysis.client.config.netty.NettyClientProperties;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/1 11:56
 */
@Component
@Slf4j
public class ReconnectHandler extends ChannelInboundHandlerAdapter {

    @Resource
    NettyClientProperties nettyClientProperties;

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyClient nettyClient = SpringBeanHolder.getBean(NettyClient.class);
        if (NettyClientHolder.isExceedThreshold(nettyClientProperties.getRetryCount())) {
            log.error("reconnecting retry count exceeded threshold:[{}] , preparing automatic exit ...",
                    nettyClientProperties.getRetryCount());
            System.exit(-1);
            return;
        }
        NettyClientHolder.setContext(null);
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(() -> {
            try {
                log.warn("preparing to reconnect ...");
                nettyClient.nettyStart();
                NettyClientHolder.clearRetryCount();
            } catch (InterruptedException e) {
                log.error("reconnecting server failed", e);
                NettyClientHolder.retryCountInc();
            }
        }, nettyClientProperties.getRetryInterval(), TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }
}
