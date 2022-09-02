package com.shareworks.codeanalysis.common.enums;

import com.shareworks.codeanalysis.common.constant.ExceptionSysConstant;
import com.shareworks.codeanalysis.common.exception.SerializationException;
import java.util.Arrays;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/30 9:08
 */
@AllArgsConstructor
@Getter
public enum SerializationTypeEnums implements BaseEnums{

    /**
     * json
     */
    JSON((byte) 0),
    /**
     * google protocol Buffers
     */
    PROTOCOL_BUFFERS((byte) 1),
    /**
     * xml
     */
    XML((byte) 2),
    ;

    private final byte type;

    public static SerializationTypeEnums get(byte type) {
        Optional<SerializationTypeEnums> optional = Arrays.stream(SerializationTypeEnums.values())
                .filter(serializationType -> serializationType.getType() == type).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new SerializationException(ExceptionSysConstant.INTERNAL_SERVER_ERROR, "无效的序列化类型");
    }

    @Override
    public String getName() {
        return this.name();
    }
}
