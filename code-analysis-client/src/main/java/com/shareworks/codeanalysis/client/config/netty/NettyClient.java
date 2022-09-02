package com.shareworks.codeanalysis.client.config.netty;

import com.shareworks.codeanalysis.client.config.netty.handler.NettyClientServerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.Objects;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 16:59
 */
@Component
@Slf4j
public class NettyClient {

    @Resource
    private NettyClientProperties nettyClientProperties;

    @Resource
    NettyClientServerInitializer nettyClientServerInitializer;

    @Resource
    NettyConnectionListener nettyConnectionListener;

    Bootstrap bootstrap;
    NioEventLoopGroup group;

    private void init() {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap
                // 设置线程组
                .group(group)
                // 设置为NIO模式
                .channel(NioSocketChannel.class)
                .handler(nettyClientServerInitializer);
    }

    private ChannelFuture start() throws InterruptedException {
        this.init();
        return this.bootstrap
                .connect(nettyClientProperties.getIpAddress(), nettyClientProperties.getPort())
                .addListener(nettyConnectionListener)
                .sync();
    }

    public void shutdown(ChannelFuture channelFuture) throws InterruptedException {
        log.info("netty client connect completed address: ["
                + this.nettyClientProperties.getIpAddress()
                + "] , port: [" + this.nettyClientProperties.getPort() + "]");
        channelFuture.channel().closeFuture().sync();
    }

    private void close() {
        if (Objects.isNull(NettyClientHolder.getContext())) {
            return;
        }
        ChannelFuture closeChannelFuture = NettyClientHolder.getContext().close();
        closeChannelFuture.addListener((ChannelFutureListener) channelFuture -> {
            if (channelFuture.isSuccess()) {
                log.info("netty connection closed successfully");
                return;
            }
            log.error("netty connection closed failed");
        });
    }

    public void nettyStart() throws InterruptedException {
        ChannelFuture channelFuture = start();
        if (!NettyClientHolder.isStartFlag()) {
            NettyClientHolder.setStartFlag(true);
            // 在JVM销毁前关闭服务
            Runtime.getRuntime().addShutdownHook(new Thread(this::close));
        }
        this.shutdown(channelFuture);
    }
}
