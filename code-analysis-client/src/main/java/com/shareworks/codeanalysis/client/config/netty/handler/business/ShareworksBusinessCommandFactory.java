//package com.shareworks.codeanalysis.client.config.netty.handler.business;
//
//import cn.hutool.core.collection.CollectionUtil;
//import com.shareworks.codeanalysis.common.constant.ExceptionSysConstant;
//import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
//import com.shareworks.codeanalysis.common.exception.BusinessException;
//import com.shareworks.codeanalysis.common.message.dto.ShareworksBaseDTO;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//import javax.annotation.PostConstruct;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Component;
//
///**
// * @author martin.peng
// * @Desc
// * @date 2022/8/31 10:50
// */
//@Component
//@AllArgsConstructor
//public class ShareworksBusinessCommandFactory {
//
//    private final List<ShareworksCommandBusiness<? extends ShareworksBaseDTO>> shareworksBusinessList;
//
//    private static final Map<CommandTypeEnums, ShareworksCommandBusiness<? extends ShareworksBaseDTO>> COMMAND_TYPE_ENUMS_SHAREWORKS_BUSINESS_MAP = new ConcurrentHashMap<>();
//
//    @PostConstruct
//    public void init() {
//        if (CollectionUtil.isEmpty(shareworksBusinessList)) {
//            return;
//        }
//        for (ShareworksCommandBusiness<? extends ShareworksBaseDTO> shareworksBusiness : shareworksBusinessList) {
//            COMMAND_TYPE_ENUMS_SHAREWORKS_BUSINESS_MAP.put(shareworksBusiness.getCommandType(), shareworksBusiness);
//        }
//    }
//
//    public static ShareworksCommandBusiness<? extends ShareworksBaseDTO> getShareworksBusiness(
//            CommandTypeEnums commandType) {
//        ShareworksCommandBusiness<? extends ShareworksBaseDTO> shareworksBusiness = COMMAND_TYPE_ENUMS_SHAREWORKS_BUSINESS_MAP.get(
//                commandType);
//        if (Objects.isNull(shareworksBusiness)) {
//            throw new BusinessException(ExceptionSysConstant.INTERNAL_SERVER_ERROR,
//                    "unknown command type: " + commandType);
//        }
//        return shareworksBusiness;
//    }
//}
