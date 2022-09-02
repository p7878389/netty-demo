package com.shareworks.codeanalysis.common.enums;

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
public enum SerializerTypeEnums implements BaseEnums {

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

    public static SerializerTypeEnums get(byte type) {
        Optional<SerializerTypeEnums> optional = Arrays.stream(SerializerTypeEnums.values())
                .filter(serializationType -> serializationType.getType() == type).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw null;
    }

    @Override
    public String getName() {
        return this.name();
    }
}
