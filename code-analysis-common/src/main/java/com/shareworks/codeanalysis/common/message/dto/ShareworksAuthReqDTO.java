package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.handler.serializer.ShareworksSerializer;
import com.shareworks.codeanalysis.common.handler.serializer.ShareworksSerializerFactory;
import com.shareworks.codeanalysis.common.message.ShareworksMessageConvert;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 16:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ShareworksAuthReqDTO extends ShareworksBaseDTO implements ShareworksMessageConvert {

    private String appKey;

    private String appSecret;

    @Override
    public CommandTypeEnums getCommandType() {
        return CommandTypeEnums.AUTHENTICATION;
    }

    @Override
    public byte[] serializationBody(ShareworksEncodeDTO encodeDTO) {
        if (StringUtils.isBlank(getTraceId())) {
            setTraceId(SessionIdUtils.generateTraceId());
        }
        ShareworksSerializer shareworksSerializer = ShareworksSerializerFactory.getSerializationType(
                encodeDTO.getSerializationType());
        return shareworksSerializer.serialize(this);
    }
}
