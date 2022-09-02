package com.shareworks.codeanalysis.server.config.netty;

import io.netty.channel.ChannelFuture;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:20
 */
@Component
@Slf4j
public class NettyStartListener implements CommandLineRunner {

    @Resource
    private NettyServer nettyServer;

    @Override
    public void run(String... args) throws Exception {
        ChannelFuture channelFuture = this.nettyServer.start();
        // 在JVM销毁前关闭服务
        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.close()));
        log.info("netty server bootstrap completed port " + nettyServer.getPort());
        channelFuture.channel().closeFuture().sync();
    }
}
