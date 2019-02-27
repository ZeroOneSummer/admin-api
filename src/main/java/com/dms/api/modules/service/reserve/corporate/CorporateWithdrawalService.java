package com.dms.api.modules.service.reserve.corporate;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.R;

import java.util.Map;

/**
 * @Author: hushuangxi
 * @CreateDate: 2018/9/26 11:40
 * @Description: 作用描述
 */
public interface CorporateWithdrawalService {

    //服务商的预申请对公提现，先调I服务的预申请对公提现，再调审批服务的对公提现申请
    R getwithdrawal(String url, Map<String, String> reqParams, JSONObject aduitParams);

    //查询我的审批订单
    JSONObject queryAuditList(String url, Map<String, String> reqParams);

    //对公提现审批驳回,先调I服务的对公提现驳回，再调审批服务的驳回
    R rejectwithdrawal(String url, Map<String, String> reqParams, Map<String, String> aduitParams);

    //渠道专员和出纳审批同意,只调用审批服务
    R getPassMyAudit(String url, Map<String, String> auditReqParams);

    //财务经理审批同意，先调用审批服务，再调用I服务的确认对公打款
    R getPassMyAuditAndPay(String url, Map<String, String> auditReqParams, Map<String, String> reqParams);

}
