package com.shareworks.codeanalysis.server.config.netty;

import com.shareworks.codeanalysis.common.handler.ShareworksMessageDecoder;
import com.shareworks.codeanalysis.common.handler.ShareworksMessageEncoder;
import com.shareworks.codeanalysis.server.config.netty.handler.AuthorizationResponseHandler;
import com.shareworks.codeanalysis.server.config.netty.handler.GlobalExceptionHandler;
import com.shareworks.codeanalysis.server.config.netty.handler.HeartBeatResponseHandler;
import com.shareworks.codeanalysis.server.config.netty.handler.ServerBusinessHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:16
 */
@AllArgsConstructor
public class NettySocketServerInitializer extends ChannelInitializer<SocketChannel> {

    private final AuthorizationResponseHandler authorizationResponseHandler;
    private final HeartBeatResponseHandler heartBeatResponseHandler;
    private final ServerBusinessHandler serverBusinessHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 30, 60, TimeUnit.SECONDS));
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 2, 2, 0, 2));
        // 添加对byte数组的编解码，netty提供了很多编解码器，你们可以根据需要选择
        pipeline.addLast(new ShareworksMessageDecoder());
        pipeline.addLast(new ShareworksMessageEncoder());
        // 添加上自己的处理器
        pipeline.addLast(authorizationResponseHandler);
        pipeline.addLast(heartBeatResponseHandler);
        pipeline.addLast(new GlobalExceptionHandler());
        pipeline.addLast(serverBusinessHandler);
    }
}
