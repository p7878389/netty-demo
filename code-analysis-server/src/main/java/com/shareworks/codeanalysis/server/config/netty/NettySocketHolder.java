package com.shareworks.codeanalysis.server.config.netty;

import io.netty.channel.Channel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 11:15
 */
public class NettySocketHolder {

    private static final Map<Channel, NettyChannelDTO> NETTY_CHANNEL_MAP = new ConcurrentHashMap<>();

    public static NettyChannelDTO get(Channel channel) {
        return NETTY_CHANNEL_MAP.get(channel);
    }

    public static void put(Channel channel, NettyChannelDTO nettyChannelDTO) {
        NETTY_CHANNEL_MAP.put(channel, nettyChannelDTO);
    }

    public static void remove(Channel channel) {
        NETTY_CHANNEL_MAP.remove(channel);
    }
}
