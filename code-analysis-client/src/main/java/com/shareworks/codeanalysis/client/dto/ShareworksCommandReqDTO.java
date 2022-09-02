package com.shareworks.codeanalysis.client.dto;

import com.shareworks.codeanalysis.common.enums.CommandTypeEnums;
import java.io.Serializable;
import lombok.Data;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 9:48
 */
@Data
public class ShareworksCommandReqDTO implements Serializable {

    private CommandTypeEnums commandType;
}
