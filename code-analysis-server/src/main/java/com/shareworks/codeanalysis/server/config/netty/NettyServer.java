package com.shareworks.codeanalysis.server.config.netty;

import com.shareworks.codeanalysis.server.config.netty.handler.AuthorizationResponseHandler;
import com.shareworks.codeanalysis.server.config.netty.handler.HeartBeatResponseHandler;
import com.shareworks.codeanalysis.server.config.netty.handler.ServerBusinessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.net.InetSocketAddress;
import javax.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:18
 */
@Slf4j
@Component
@Getter
public class NettyServer {

    @Resource
    private AuthorizationResponseHandler authorizationResponseHandler;
    @Resource
    private HeartBeatResponseHandler heartBeatResponseHandler;
    @Resource
    private ServerBusinessHandler serverBusinessHandler;

    private ServerBootstrap serverBootstrap;

    NioEventLoopGroup bossGroup;

    NioEventLoopGroup workerGroup;
    /**
     * netty服务监听端口
     */
    @Value("${netty.port:8088}")
    private int port;
    /**
     * 主线程组数量
     */
    @Value("${netty.bossThread:1}")
    private int bossThread;

    /**
     * 启动netty服务器
     */
    public ChannelFuture start() throws InterruptedException {
        this.init();
        return this.serverBootstrap.bind().sync();
    }

    /**
     * 初始化netty配置
     */
    private void init() {
        // 创建两个线程组，bossGroup为接收请求的线程组，一般1-2个就行
        bossGroup = new NioEventLoopGroup(this.bossThread);
        // 实际工作的线程组
        workerGroup = new NioEventLoopGroup();
        this.serverBootstrap = new ServerBootstrap();
        this.serverBootstrap.group(bossGroup, workerGroup)
                // 配置为nio类型
                .channel(NioServerSocketChannel.class)
                .localAddress(new InetSocketAddress(port))
                //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                //设置TCP长连接,一般如果两个小时内没有数据的通信时,TCP会自动发送一个活动探测数据报文
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new NettySocketServerInitializer(authorizationResponseHandler
                        , heartBeatResponseHandler, serverBusinessHandler)); // 加入自己的初始化器
    }

    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        log.info("======Shutdown Netty Server Success!=========");
    }
}
