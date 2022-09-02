package com.shareworks.codeanalysis.common.enums;

import com.shareworks.codeanalysis.common.message.dto.ShareworksAuthReqDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksAuthRespDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksHeartbeatReqDTO;
import com.shareworks.codeanalysis.common.message.dto.ShareworksHeartbeatRespDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/29 15:35
 */
@AllArgsConstructor
@Getter
public enum CommandTypeEnums implements BaseEnums {

    /**
     * 心跳检测
     */
    PING((byte) 0, ShareworksHeartbeatReqDTO.class),
    /**
     * 心跳检测响应
     */
    PING_ACK((byte) 1, ShareworksHeartbeatRespDTO.class),
    /**
     * 鉴权
     */
    AUTHENTICATION((byte) 2, ShareworksAuthReqDTO.class),
    /**
     * 鉴权响应
     */
    AUTHENTICATION_ACK((byte) 3, ShareworksAuthRespDTO.class),
    /**
     * 拉取任务
     */
    PULL_TASK((byte) 4, null),
    /**
     * 拉取任务响应
     */
    PULL_TASK_ACK((byte) 5, null),
    /**
     * 推送分析报告
     */
    PUSH_ANALYZE_REPORT((byte) 6, null),

    /**
     * 推送分析报告响应
     */
    PUSH_ANALYZE_REPORT_ACK((byte) 7, null),
    PONG((byte) 8, ShareworksHeartbeatReqDTO.class),
    PONG_ACK((byte) 9, ShareworksHeartbeatRespDTO.class),

    ;

    private final byte type;
    private final Class<? extends ShareworksBaseDTO> clazz;

    public static CommandTypeEnums get(byte type) {
        for (CommandTypeEnums value : values()) {
            if (value.type == type) {
                return value;
            }
        }
        throw new RuntimeException("unsupported type: " + type);
    }

    @Override
    public String getName() {
        return this.name();
    }
}
