package com.dms.api.modules.entity.reserve.corporate;

import lombok.Data;

/**
 * @author Administrator
 * @Description: 我的审批订单
 * @date 2018/9/7 09:40
 */
@Data
public class CorporateBankCardEntity {

    //请求流水号
    private String requestNo;

    //公司名称
    private String bankAccountName;

    //用户标识
    private String userUid;

    //银行卡名称
    private String bankName;

    //银行卡编码
    private String bankCode;

    //银行卡号
    private String bankAccountNo;

    //银行卡支行地址
    private String bankAccountAddress;

    //省份
    private String province;

    //市
    private String city;

    //提现业务流水号
    private String withdrawSerialOrderNo;

    //绑定标识
    private String bankCardSerialNo;

    //提现单号
    private String withdrawOrderNo;

    //金额
    private String amt;

    //绑定ID
    private String bindId;

    //账号类型
    private String bankAccountType;

    //渠道编号
    private String channelType;

}
