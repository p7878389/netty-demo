package com.shareworks.codeanalysis.common.message.dto;

import com.shareworks.codeanalysis.common.message.ShareworksMessageConvert;
import com.shareworks.codeanalysis.common.utils.SessionIdUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 16:58
 */
@Data
public abstract class ShareworksBaseDTO implements ShareworksMessageConvert {

    /**
     * 请求内容签名
     */
    private String signature;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 请求ID
     */
    private String traceId;

    private int errorCode = 200;

    private String errorMsg;

    public boolean isSucceeded() {
        return errorCode == 200;
    }

    @Override
    public byte[] serializationBody(ShareworksEncodeDTO encodeDTO) {
        if (StringUtils.isBlank(getTraceId())) {
            setTraceId(SessionIdUtils.generateTraceId());
        }
        return ShareworksMessageConvert.super.serializationBody(encodeDTO);
    }
}
