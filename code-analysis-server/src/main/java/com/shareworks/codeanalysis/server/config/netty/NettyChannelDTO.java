package com.shareworks.codeanalysis.server.config.netty;

import com.shareworks.codeanalysis.common.enums.SerializerTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import io.netty.channel.Channel;
import lombok.Builder;
import lombok.Data;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 14:37
 */
@Data
@Builder
public class NettyChannelDTO {

    private String sessionId;

    private String appKey;

    private SignTypeEnums signType;

    private String signKey;

    private Channel channel;

    private SerializerTypeEnums serializationType;

    private byte version;
}
