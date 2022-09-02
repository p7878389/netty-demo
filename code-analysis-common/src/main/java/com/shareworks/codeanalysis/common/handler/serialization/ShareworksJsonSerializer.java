package com.shareworks.codeanalysis.common.handler.serialization;

import com.alibaba.fastjson.JSONObject;
import com.shareworks.codeanalysis.common.enums.SerializationTypeEnums;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 9:18
 */
public class ShareworksJsonSerializer implements ShareworksSerializer {

    protected static final ShareworksJsonSerializer INSTANCE = new ShareworksJsonSerializer();

    private ShareworksJsonSerializer() {
    }

    @Override
    public byte[] serialize(Object data) {
        if (Objects.isNull(data)) {
            return new byte[0];
        }
        return JSONObject.toJSONString(data).getBytes(Charset.defaultCharset());
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSONObject.parseObject(data, clazz);
    }

    @Override
    public SerializationTypeEnums getSerializationType() {
        return SerializationTypeEnums.JSON;
    }
}
