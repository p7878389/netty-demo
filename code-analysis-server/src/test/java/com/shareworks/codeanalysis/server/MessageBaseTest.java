package com.shareworks.codeanalysis.server;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.shareworks.codeanalysis.common.enums.SignTypeEnums;
import com.shareworks.codeanalysis.common.message.dto.ShareworksHeartbeatReqDTO;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.nio.charset.Charset;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/9/1 9:00
 */
public class MessageBaseTest {

    String privateKey = null;
    String publicKey = null;
    ByteBuf byteBuf;

    int magicNumber;
    String sessionId;
    String traceId;
    SignTypeEnums signType;

    byte[] signBytes;
    byte[] bodyByte;

    ByteBuf buf;

    @BeforeEach
    public void setUp() {
        privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIFbLcgzDxaLu8C9akmXpmLnTVKoq6muLeMcvSVTkfKkM22OjC+32GxeIetvJE9Q7aa+tTAcuLevt4Xf+zpR+Dd+GgRQPrC9j3bBFQ/BfC6qGTl9lKdrc43AsoZvRqF972HDyrpPQ7yMLYBsPRSV0acLqT7q07gGELISytWrh+O1AgMBAAECgYBSQva5YucMcm5C4vINvNf9+5QrpcINFQxvap5Kd3vP72YZHNa/nc2PFMnDXBmeKxwGty6oWwuJHMC77TVp4CjV0SzkARqg+cThALOiDQy8fnbxlZqJoGIfh/cLIBBjKV77r08kv++TTy6yYmCUMe/R3nZx9lGHsrCW86C8pX/TAQJBAMV6nO7RV8mwxKeSil7oCRmlmWB6RCHHPxraN8cgOUArSYr3/C1ILEyMF5owQTT6aSaDmhokXjs1XTPZfH9B0fUCQQCnsI1yDpyPd0ORf80rAk8H6Gl8OxnsKu3DEs7B7UZOOhjuxUQIOBiYN57IHjFvUxy2MrsW2PKPgzUmWEawU/LBAkB9Z7nU5tgK2thnWeXJEy0EPmdCHyK5GKB4Wu9k4o+4skSHnadk0hpfme87GT2veP3hWV05ELNOgjrnvEsVPSUpAkBC1KtapJYi205Tg5K6LF5g8XBw8YEATY+aqjk6spzNenwLVRUrDWhH6grfCKF9E6uitY84qNfq+EjLcm0hwyNBAkBmFqy1BJBDTPEyPcSWgvAWlStO/q40VMLyR9B4D0irkWl/B6UyZwYIIUlClbDKwWetsrMNw54CtGFwWL29UFQ9";
        publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCBWy3IMw8Wi7vAvWpJl6Zi501SqKupri3jHL0lU5HypDNtjowvt9hsXiHrbyRPUO2mvrUwHLi3r7eF3/s6Ufg3fhoEUD6wvY92wRUPwXwuqhk5fZSna3ONwLKGb0ahfe9hw8q6T0O8jC2AbD0UldGnC6k+6tO4BhCyEsrVq4fjtQIDAQAB";
    }

    @BeforeEach
    public void setUp1() {
        magicNumber = RandomUtil.randomInt(4);
        sessionId = SessionIdUtils.generateId();
        traceId = SessionIdUtils.generateTraceId();
        signType = SignTypeEnums.MD5_WITH_RSA;
        buf = Unpooled.buffer();
        // 魔数
        buf.writeInt(magicNumber);
        // 版本号
        buf.writeByte((byte) 1);
        // 序列化类型
        buf.writeByte((byte) 0);
        // 签名类型
        buf.writeByte(signType.getType());
        // sessionId
        buf.writeCharSequence(sessionId, Charset.defaultCharset());
        // 消息类型
        buf.writeByte((byte) 0);
        ShareworksHeartbeatReqDTO heartbeatReqDTO = new ShareworksHeartbeatReqDTO();
        heartbeatReqDTO.setTraceId(traceId);
        String jsonString = JSONObject.toJSONString(heartbeatReqDTO);
        byte[] bodyByte = jsonString.getBytes(Charset.defaultCharset());
        buf.writeInt(bodyByte.length);
        buf.writeBytes(bodyByte);

        byte[] signBytes = signType.getSignTypeStrategy().apply(bodyByte, privateKey);
        buf.writeInt(signBytes.length);
        buf.writeBytes(signBytes);

        byteBuf = buf.duplicate();
    }
}
