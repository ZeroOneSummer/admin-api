package com.dms.api.modules.service.reserve.corporate;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.R;

import java.util.List;
import java.util.Map;

/**
 * @Author: hushuangxi
 * @CreateDate: 2018/9/26 11:40
 * @Description: 作用描述
 */
public interface CorporateBankCardService {

    JSONObject queryList(String url, Map<String, String> reqParams);

    List<Map<String, String>> getOrgCodes(Long orgId);

    //对公绑卡
    R BindBankCard(String url, Map<String, String> reqParams);

    //对公解绑银行卡
    R UntieBankCard(String url, Map<String, String> reqParams);

    List<Map<String, String>> getBankCodes();

    //调用I接口查询服务商在B系统的可出资金
    JSONObject getAllowWithdrawal(String url,Map<String,String> params);
}
