package com.dms.api.modules.controller.reserve.corporate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.reserve.corporate.CorporateBankCardEntity;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.reserve.corporate.CorporateBankCardService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 对公业务controller
 */
@RequestMapping("/corporate/bankcard")
@RestController
public class CorporateBankCardController extends AbstractController {

    @Autowired
    CorporateBankCardService corporateBankCardService;

    /**
     *
     * 查询列表数据
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("corporate:bankcard:list")
    public R list(@RequestParam Map<String,Object> params) {
        logger.info("CorporateBankCardController-list入参：{}", params);

        //获取登录用户信息
        SysUserEntity user = getUser();

        //角色是超级管理员或系统超级管理员
        if (user.isSuperAdmin() || user.isSysSuperAdmin()){
            Long orgId = Long.parseLong((String) params.get("orgId"));
            user.setOrgId(orgId);
        } else{
            logger.info("所属机构ID：{}", user.getOrgId());
            if (user.getOrgId() == null){
                logger.info("该用户没有对应归属机构");
                return null;
            }
        }

        //获取orgId对应的userUid
        List<Map<String, String>> list = corporateBankCardService.getOrgCodes(user.getOrgId());
        String userUid = list.size() == 0 ? null : list.get(0).get("userUid");
        //String loginCode = list.size() == 0 ? null : list.get(0).get("loginCode");

        if (StringUtils.isBlank(userUid)){
            logger.info("该机构没有对应的userUid");
            return null;
        }

        //请求参数
        Map<String,String> reqParams = new HashMap<>();
        reqParams.put("userUid",userUid);
        String url = HostsConfig.hostsI + ConstantTable.BINDCARD_LIST;

        JSONObject result = corporateBankCardService.queryList(url, reqParams);
        if (result == null) return R.ok().put("page", null);

        //json数组转化为list
        String jsonArrayStr = result.getString("data");
        List<CorporateBankCardEntity> corporateBankCardEntitys = JSON.parseArray(jsonArrayStr, CorporateBankCardEntity.class);

        //return R.ok().put("page", corporateBankCardEntitys).put("loginCode",loginCode);
        return R.ok().put("page", corporateBankCardEntitys);
    }


    /**
     * 对公绑卡
     */
    @RequestMapping("/bindcard")
    @RequiresPermissions("corporate:bankcard:bindcard")
    public R bindcard(@RequestBody Map<String, Object> params){
        logger.info("绑定银行卡信息入参：{}",params);

        //获取登录用户信息
        SysUserEntity user = getUser();
        //不是超级管理员与系统超级管理员
        if (!user.isSuperAdmin() && !user.isSysSuperAdmin()){
            logger.info("用户登录的角色：{}",user);
            return R.error("不是管理员，无对公绑卡权限");
        }

        Long orgId = Long.parseLong((String) params.get("orgId"));
        user.setOrgId(orgId);

        logger.info("所属机构ID：{}", user.getOrgId());
        //获取orgId对应的userUid
        List<Map<String, String>> list = corporateBankCardService.getOrgCodes(user.getOrgId());
        String userUid = list.size() == 0 ? null : list.get(0).get("userUid");
        if (StringUtils.isBlank(userUid)){
            logger.info("该用户没有对应的userUid");
            return null;
        }

        //参数校验
        String requestNo = String.valueOf(System.currentTimeMillis());
        String bankAccountName = params.get("bankAccountName") == null ? null : (String) params.get("bankAccountName");
        String bankName = params.get("bankName") == null ? null : (String) params.get("bankName");
        String bankCode = params.get("bankCode") == null ? null : (String) params.get("bankCode");
        String bankAccountNo = params.get("bankAccountNo") == null ? null : (String) params.get("bankAccountNo");
        String bankAccountAddress = params.get("bankAccountAddress") == null ? null : (String) params.get("bankAccountAddress");
        String province = params.get("province") == null ? null : (String) params.get("province");
        String city = params.get("city") == null ? null : (String) params.get("city");

        if (StringUtils.isBlank(requestNo)
                || StringUtils.isBlank(bankAccountName)
                || StringUtils.isBlank(bankName)
                || StringUtils.isBlank(bankCode)
                || StringUtils.isBlank(bankAccountNo)
                || StringUtils.isBlank(bankAccountAddress)
                || StringUtils.isBlank(province)
                || StringUtils.isBlank(city)
        ){

            logger.info("参数验证失败：必要参数缺失");
            return R.error("必要参数缺失");
        }

        //请求地址和参数
        String url = HostsConfig.hostsI + ConstantTable.BIND_CARD;
        Map<String,String> reqParams = new HashMap<String,String>();
        reqParams.put("requestNo",requestNo);//请求流水号
        reqParams.put("bankAccountName",bankAccountName);//公司名称
        reqParams.put("bankAccountType","PUBLIC");//账号类型，固定填PUBLIC
        reqParams.put("userUid",userUid);//用户标识
        reqParams.put("bankName",bankName);//银行名称
        reqParams.put("bankCode",bankCode);//银行卡编码
        reqParams.put("channelType","YIBAO");//渠道编号，固定填YIBAO
        reqParams.put("bankAccountNo",bankAccountNo);//银行卡号
        reqParams.put("bankAccountAddress",bankAccountAddress);//省份
        reqParams.put("province",province);//省份
        reqParams.put("city",city);//城市

        return corporateBankCardService.BindBankCard(url,reqParams);
    }


    /**
     * 对公解绑
     */
    @RequestMapping("/unbindcard")
    @RequiresPermissions("corporate:bankcard:unbindcard")
    public R untiecard(@RequestBody Map<String, Object> params){
        logger.info("对公解绑银行卡信息入参：{}",params);

        //获取登录用户信息
        String userUid = params.get("userUid") == null ? null : (String) params.get("userUid");
        SysUserEntity user = getUser();

        //不是超级管理员与系统超级管理员
        if (!user.isSuperAdmin() && !user.isSysSuperAdmin()){
            logger.info("用户登录的角色：{}",user);
            return R.error("不是管理员，无对公解绑权限");
        }

        if (StringUtils.isBlank(userUid)){
            logger.info("该用户所属机构没有对应的userUid");
            return null;
        }
        //参数校验
        String requestNo = String.valueOf(System.currentTimeMillis());

        if (StringUtils.isBlank(requestNo)){
            logger.info("参数验证失败：必要参数缺失");
            return R.error("必要参数缺失");
        }

        //请求地址和参数
        String url = HostsConfig.hostsI + ConstantTable.UNBIND_CARD;
        Map<String,String> reqParams = new HashMap<>();
        reqParams.put("requestNo",requestNo);//请求流水号
        reqParams.put("userUid",userUid);//用户标识
        reqParams.put("channelType","YIBAO");//渠道编号

        logger.info("对公解绑操作");
        return corporateBankCardService.UntieBankCard(url,reqParams);

    }

    /**
     *
     * 获取银行卡编码列表
     * @return
     */
    @RequestMapping("/getBankCodes")
    public JSONObject getBankCodes() {
        //响应格式
        JSONObject respJson = new JSONObject();
        respJson.put("msg", "FAILURE");
        respJson.put("rc", -1);
        respJson.put("data", null);
        try {
            //查询列表
            List<Map<String,String>> bankCodes = corporateBankCardService.getBankCodes();
            respJson.put("msg", "SUCCESS");
            respJson.put("rc", 200);
            respJson.put("data", bankCodes);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("获取银行卡编码列表失败");
        }
        return respJson;
    }

    /**
     *
     * 获取机构orgId列表
     * @return
     */
    @RequestMapping("/getOrgIds")
    public JSONObject getOrgIds() {
        //响应格式
        JSONObject respJson = new JSONObject();
        respJson.put("msg", "FAILURE");
        respJson.put("rc", -1);
        respJson.put("data", null);
        try {
            //查询列表
            List<Map<String,String>> orgIds = null;
            SysUserEntity user = getUser();
            if (user.isSysSuperAdmin() || user.isSuperAdmin()){
                logger.info("管理员获取机构orgId列表");
                orgIds = corporateBankCardService.getOrgCodes(null);
            }else{
                logger.info("服务商获取机构orgId列表");
                if(user.getOrgId() == null){
                    logger.info("该用户无归属机构");
                    return null;
                }
                orgIds = corporateBankCardService.getOrgCodes(user.getOrgId());
            }

            respJson.put("msg", "SUCCESS");
            respJson.put("rc", 200);
            respJson.put("data", orgIds);
        }catch (Exception e){
            e.printStackTrace();
            logger.info("获取机构orgId列表列表失败");
        }
        return respJson;
    }

    /**
     * 调用I服务查询服务商在B系统的可出金额
     * @param params
     * @return
     */
    @RequestMapping("/getAllowWithdrawal")
    public JSONObject getAllowWithdrawal(@RequestBody JSONObject params) {
        logger.info("查询服务商在B系统的可出资金入参：params: {}",params);
        String url = HostsConfig.hostsI+ConstantTable.LOGINQUERY_ALLOWOWWITHDRAWAL;

        String loginCode = params.get("loginCode") == null ? null : (String) params.get("loginCode");
        String password = params.get("password") == null ? null : (String) params.get("password");
        Map<String,String> reqParams = new HashMap<>();

        reqParams.put("logincode",loginCode);//登录账户
        reqParams.put("password",password);//用户密码
        reqParams.put("serialcode","");

        return corporateBankCardService.getAllowWithdrawal(url,reqParams);
    }


}



