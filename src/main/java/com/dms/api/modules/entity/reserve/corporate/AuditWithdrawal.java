package com.dms.api.modules.entity.reserve.corporate;

import lombok.Data;

/**
 * @program: admin
 * @Date: 2018/10/16 0016 15:27
 * @Author: hushuangxi
 * @Description:
 */
@Data
public class AuditWithdrawal {

    //用户标识
    private String userUid;

    //绑定标识
    private String bankCardSerialNo;

    //提现业务流水号
    private String withdrawSerialOrderNo;

    //提现金额
    private String amt;

    //申请人备注
    private String remark;

    //审核订单id
    private String id;

    //提现单号
    private String withdrawOrderNo;

    //请求流水号
    private String requestNo;

    //申请时间
    private String applyTime;

    //审核意见
    private String applyStatus;

    //审核组名称
    private String name;

    //创建时间
    private String createTime;

}
