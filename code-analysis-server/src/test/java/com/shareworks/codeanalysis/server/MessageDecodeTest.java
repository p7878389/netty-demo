package com.shareworks.codeanalysis.server;

import static org.junit.jupiter.api.Assertions.assertTrue;

import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.Sign;
import cn.hutool.crypto.asymmetric.SignAlgorithm;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import com.shareworks.codeanalysis.common.handler.ShareworksMessageDecoder;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import io.netty.channel.embedded.EmbeddedChannel;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 15:48
 */
public class MessageDecodeTest extends MessageBaseTest {

    @Test
    public void messageDecodeHandlerTest() {
        //2、创建EmbeddedChannel，并添加一个Decoder（我们的要测试 ChannelHandler） 其将以3字节帧长度被测试
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ShareworksMessageDecoder());
        //3、将数据写入 EmbeddedChannel
        boolean writeInbound = embeddedChannel.writeInbound(byteBuf.retain());
        assertTrue(writeInbound);
        //4、标记 Channel 为已完成状态
        boolean finish = embeddedChannel.finish();
        assertTrue(finish);

        //5、读取数据
        ShareworksMessage<ShareworksBaseDTO> shareworksMessage = embeddedChannel.readInbound();
        Assertions.assertEquals(shareworksMessage.getSessionId(), sessionId, "sessionId 解码错误");
        Assertions.assertEquals(shareworksMessage.getMessageContent().getTraceId(), traceId, "traceId 解码错误");
        Assertions.assertEquals(shareworksMessage.getMessageContent().getSignature(),
                new String(signBytes, Charset.defaultCharset()), "sign 解码错误");
        if (SignTypeEnums.MD5_WITH_RSA.equals(shareworksMessage.getSignType())) {
            Sign sign = SecureUtil.sign(SignAlgorithm.MD5withRSA, privateKey, publicKey);
            byte[] decodeHex = HexUtil.decodeHex(shareworksMessage.getMessageContent().getSignature());
            boolean verify = sign.verify(bodyByte, decodeHex);
            System.out.println(verify);
        }
        Assertions.assertEquals(shareworksMessage.getMagicNumber(), magicNumber, "magicNumber 解码错误");
        //释放资源
        buf.release();
    }
}
