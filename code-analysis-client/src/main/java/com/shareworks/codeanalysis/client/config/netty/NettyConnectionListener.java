package com.shareworks.codeanalysis.client.config.netty;

import com.shareworks.codeanalysis.client.config.SpringBeanHolder;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/1 11:46
 */
@Slf4j
@Component
public class NettyConnectionListener implements ChannelFutureListener {


    @Resource
    private NettyClientProperties nettyClientProperties;

    @Override
    public void operationComplete(ChannelFuture channelFuture) {
        if (channelFuture.isSuccess()) {
            log.info("reconnecting to server successfully");
            return;
        }

        if (NettyClientHolder.isExceedThreshold(nettyClientProperties.getRetryCount())) {
            log.error("reconnecting retry count exceeded threshold:[{}] , preparing automatic exit ...",
                    nettyClientProperties.getRetryCount());
            System.exit(-1);
            return;
        }

        log.warn("preparing to reconnecting to server");
        final EventLoop loop = channelFuture.channel().eventLoop();
        loop.schedule(() -> {
            try {
                NettyClient nettyClient = SpringBeanHolder.getBean(NettyClient.class);
                NettyClientHolder.retryCountInc();
                nettyClient.nettyStart();
                NettyClientHolder.clearRetryCount();
            } catch (Exception e) {
                log.error("reconnecting to server failed", e);
            }
        }, nettyClientProperties.getRetryInterval(), TimeUnit.SECONDS);

    }
}
