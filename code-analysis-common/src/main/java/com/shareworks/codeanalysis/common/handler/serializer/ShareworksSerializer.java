package com.shareworks.codeanalysis.common.handler.serializer;

import com.shareworks.codeanalysis.common.enums.SerializerTypeEnums;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 9:15
 */
@SuppressWarnings("JavadocDeclaration")
public interface ShareworksSerializer {

    /**
     * 序列化
     *
     * @param data
     * @return
     */
    byte[] serialize(Object data);

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);

    /**
     * 序列化类型
     *
     * @return
     */
    SerializerTypeEnums getSerializerType();
}
