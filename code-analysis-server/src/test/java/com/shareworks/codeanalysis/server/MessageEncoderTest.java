package com.shareworks.codeanalysis.server;

import cn.hutool.core.util.RandomUtil;
import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.enums.SerializationTypeEnums;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import com.shareworks.codeanalysis.common.handler.ShareworksMessageDecoder;
import com.shareworks.codeanalysis.common.handler.ShareworksMessageEncoder;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksAuthReqDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/1 8:54
 */
public class MessageEncoderTest extends MessageBaseTest {

    ShareworksMessage<ShareworksBaseDTO> message;

    @BeforeEach
    public void setUp2() {
        message = new ShareworksMessage<>();
        message.setMagicNumber(RandomUtil.randomInt(4));
        message.setMainVersion((byte) 1);
        message.setSerializationType(SerializationTypeEnums.JSON);
        message.setSignType(SignTypeEnums.MD5_WITH_RSA);
        message.setSignKey(privateKey);
        message.setSessionId(SessionIdUtils.generateId());
        message.setCommandType(CommandTypeEnums.AUTHENTICATION);
        message.setMessageContent(new ShareworksAuthReqDTO());
    }

    @Test
    public void messageEncoderTest() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ShareworksMessageEncoder(),
                new ShareworksMessageDecoder());
        embeddedChannel.writeOutbound(message);
        embeddedChannel.finish();

        Object readOutbound = embeddedChannel.readOutbound();
        Assertions.assertTrue(readOutbound instanceof ShareworksMessage);
    }

}
