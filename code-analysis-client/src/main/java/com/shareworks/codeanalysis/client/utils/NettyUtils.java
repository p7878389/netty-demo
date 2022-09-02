package com.shareworks.codeanalysis.client.utils;

import cn.hutool.core.util.RandomUtil;
import com.shareworks.codeanalysis.client.config.netty.NettyClientHolder;
import com.shareworks.codeanalysis.client.config.netty.NettyClientProperties;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:02
 */
public class NettyUtils {

    public static <T extends ShareworksBaseDTO> ShareworksMessage<T> getSendMessage(
            NettyClientProperties nettyClientProperties, T reqDTO) {
        ShareworksMessage<T> shareworksMessage = new ShareworksMessage<>();
        shareworksMessage.setMagicNumber(RandomUtil.randomInt());
        shareworksMessage.setMainVersion(nettyClientProperties.getVersion());
        shareworksMessage.setSignType(nettyClientProperties.getSignType());
        shareworksMessage.setSignKey(nettyClientProperties.getSignKey());
        shareworksMessage.setCommandType(reqDTO.getCommandType());
        shareworksMessage.setSerializerType(nettyClientProperties.getSerializationType());
        shareworksMessage.setMessageContent(reqDTO);
        shareworksMessage.setSessionId(NettyClientHolder.getSessionId());
        return shareworksMessage;
    }
}
