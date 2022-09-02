package com.shareworks.codeanalysis.client.config.netty.handler;

import com.shareworks.codeanalysis.common.handler.ShareworksMessageDecoder;
import com.shareworks.codeanalysis.common.handler.ShareworksMessageEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:16
 */
@Component
public class NettyClientServerInitializer extends ChannelInitializer<SocketChannel> {

    @Resource
    private AuthorizationRequestHandler authorizationRequestHandler;
    @Resource
    private HeartBeatRequestHandler heartBeatRequestHandler;
    @Resource
    private BusinessHandler businessHandler;
    @Resource
    private ReconnectHandler reconnectHandler;
    @Resource
    private ClientGlobalExceptionHandler clientGlobalExceptionHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new IdleStateHandler(30, 30, 60, TimeUnit.SECONDS));
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(65535, 2, 2, 0, 2));
        // 添加对byte数组的编解码，netty提供了很多编解码器，你们可以根据需要选择
        pipeline.addLast(new ShareworksMessageDecoder());
        pipeline.addLast(new ShareworksMessageEncoder());
        // 添加上自己的处理器
        pipeline.addLast(reconnectHandler);
        pipeline.addLast(authorizationRequestHandler);
        pipeline.addLast(heartBeatRequestHandler);
        pipeline.addLast(businessHandler);
        pipeline.addLast(clientGlobalExceptionHandler);
    }
}
