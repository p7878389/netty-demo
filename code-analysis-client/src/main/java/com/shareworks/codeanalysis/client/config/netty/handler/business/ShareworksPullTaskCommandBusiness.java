//package com.shareworks.codeanalysis.client.config.netty.handler.business;
//
//import com.shareworks.codeanalysis.client.service.DynamicTaskService;
//import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
//import com.shareworks.codeanalysis.common.message.ShareworksMessage;
//import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
//import com.shareworks.codeanalysis.common.message.dto.ShareworksPullTaskReqDTO;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//
///**
// * @author martin.peng
// * @Desc
// * @date 2022/8/31 10:42
// */
//@Component("ShareworksPullTaskCommandBusiness")
//@AllArgsConstructor
//public class ShareworksPullTaskCommandBusiness implements ShareworksBusiness{
//
//    private final DynamicTaskService dynamicTaskService;
//
//
//    @Override
//    public void processCommand(ShareworksMessage<? extends ShareworksBaseDTO> shareworksMessage) {
//
//    }
//
//    @Override
//    public CommandTypeEnums getCommandType() {
//        return CommandTypeEnums.PULL_TASK;
//    }
//}
