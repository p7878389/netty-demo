package com.shareworks.codeanalysis.common.handler.serialization;

import com.shareworks.codeanalysis.common.constant.ExceptionSysConstant;
import com.shareworks.codeanalysis.common.enums.SerializationTypeEnums;
import com.shareworks.codeanalysis.common.exception.SerializationException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 9:34
 */
@SuppressWarnings("JavadocDeclaration")
public class ShareworksSerializerFactory {

    private static final Map<SerializationTypeEnums, ShareworksSerializer> SERIALIZATION_MAP = new ConcurrentHashMap<>(
            SerializationTypeEnums.values().length);

    static {
        SERIALIZATION_MAP.put(SerializationTypeEnums.JSON, ShareworksJsonSerializer.INSTANCE);
    }

    /**
     * 获取序列化方式
     *
     * @param serializationType
     * @return
     */
    public static ShareworksSerializer getSerializationType(SerializationTypeEnums serializationType) {
        if (Objects.isNull(serializationType)) {
            throw new SerializationException(ExceptionSysConstant.INTERNAL_SERVER_ERROR, "序列化方式不能为空");
        }
        ShareworksSerializer ShareworksSerializer = SERIALIZATION_MAP.get(serializationType);
        if (Objects.isNull(ShareworksSerializer)) {
            throw new SerializationException(ExceptionSysConstant.INTERNAL_SERVER_ERROR,
                    "不支持的序列化方式 type [" + serializationType.name() + "]");
        }
        return ShareworksSerializer;
    }
}
