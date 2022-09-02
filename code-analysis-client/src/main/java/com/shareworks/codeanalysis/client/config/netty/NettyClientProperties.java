package com.shareworks.codeanalysis.client.config.netty;

import com.shareworks.codeanalysis.common.enums.SerializerTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 17:02
 */
@Component
@ConfigurationProperties(prefix = "netty.client")
@Data
public class NettyClientProperties {

    private String ipAddress;

    private int port;

    private SerializerTypeEnums serializationType;

    private SignTypeEnums signType;

    private String signKey;

    private String appKey;

    private String appSecret;

    private byte version;

    private int retryCount;

    private int retryInterval;
}
