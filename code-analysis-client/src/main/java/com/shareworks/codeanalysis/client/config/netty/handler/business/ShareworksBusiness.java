package com.shareworks.codeanalysis.client.config.netty.handler.business;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import com.shareworks.codeanalysis.common.message.ShareworksMessage;
import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:41
 */
public interface ShareworksBusiness {

    /**
     * 根据指令处理业务罗
     *
     * @param shareworksMessage 消息内容
     */
    <T extends ShareworksBaseDTO> void processCommand(ShareworksMessage<T> shareworksMessage);

    /**
     * 获取指令类型
     *
     * @return 指令类型
     */
    CommandTypeEnums getCommandType();

}
