package com.dms.api.modules.controller.reserve.corporate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.Enum.AuditGroupEnum;
import com.dms.api.common.utils.*;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.reserve.corporate.AuditWithdrawal;
import com.dms.api.modules.entity.sys.user.SysUserAudit;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.reserve.corporate.CorporateBankCardService;
import com.dms.api.modules.service.reserve.corporate.CorporateWithdrawalService;
import com.dms.api.modules.service.sys.user.SysUserAuditService;
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
 * 对公提现业务controller
 */
@RequestMapping("/corporate/withdrawal")
@RestController
public class CorporateWithdrawalController extends AbstractController {

    @Autowired
    CorporateWithdrawalService corporateWithdrawalService;
    @Autowired
    CorporateBankCardService corporateBankCardService;

    @Autowired
    private CheckTraPwdUtils checkTraPwdUtils;

    @Autowired
    private SysUserAuditService sysUserAuditService;

    //预申请对公提现
    @RequestMapping("/preapplication")
    @RequiresPermissions("corporate:withdrawal:apply")
    public R getwithdrawal(@RequestBody Map<String,Object> params){
        logger.info("CorporateWithdrawalController -->预申请对公提现入参： {}",params);

        //获取登录用户信息
        SysUserEntity user = getUser();
        logger.info("所属机构ID：{}", user.getOrgId());
        if (user.getOrgId() == null){
            return R.error("该用户无归属机构");
        }

        //判断登录用户是否是服务商
        if (user.isSuperAdmin() || user.isSysSuperAdmin()){
            logger.info("用户登录的角色：{}",user);
            return R.error("不是服务商，无预申请对公提现权限");
        }

        if (!StringUtils.equalsIgnoreCase(AuditGroupEnum.FACILITATOR.getDesc(),user.getGroupId())){
            logger.info("用户登录的角色：{}",user.getGroupId());
            return R.error("该用户不是服务商，无预申请对公提现权限");
        }

        //获取该用户的可出金额
        //List<Map<String, String>>  orgList = corporateBankCardService.getOrgCodes(user.getOrgId());
        //logger.info("获取该用户的可出金额，allowWithdrawal:{}", orgList.get(0).get("allowWithdrawal"));

        //获取orgId对应的userUid
        List<Map<String, String>> list = corporateBankCardService.getOrgCodes(user.getOrgId());
        String userUid = list.size() == 0 ? null : list.get(0).get("userUid");

        if (StringUtils.isBlank(userUid)){
            logger.info("该用户没有对应的userUid");
            return null;
        }

        //参数校验
        String requestNo = String.valueOf(System.currentTimeMillis());//请求流水号
        logger.info("线程开始睡眠50毫秒");
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            logger.info("线程睡眠异常");
            e.printStackTrace();

        }
        logger.info("线程睡眠结束");
        String bankCardSerialNo = params.get("bankCardSerialNo") == null ? null : (String)params.get("bankCardSerialNo");//绑定标识
        String withdrawOrderNo = String.valueOf(System.currentTimeMillis());//提现单号
        String amt = params.get("withdrawlAmt") == null ? null : (String) params.get("withdrawlAmt");
        String remark = params.get("remark") == null ? null : (String) params.get("remark");

        if (StringUtils.isBlank(requestNo)
                || StringUtils.isBlank(bankCardSerialNo)
                || StringUtils.isBlank(withdrawOrderNo)
                || StringUtils.isBlank(amt)
                || StringUtils.isBlank(remark)
        ){

            logger.info("参数验证失败：必要参数缺失");
            return R.error("必要参数缺失");
        }

        /*Object obj = orgList.get(0).get("allowWithdrawal");
        BigDecimal b = (BigDecimal) obj;
        BigDecimal amWithdrawable =b.multiply(new BigDecimal(100));
        BigDecimal bigDecimalAmt = new BigDecimal(amt);
        //比较输入的预申请的提现金额与可出金额
        if (bigDecimalAmt.compareTo(amWithdrawable) > 0){
            return R.error("提现金额不能大于可出金额");
        }*/

        //请求地址和参数
        String url = HostsConfig.hostsI + ConstantTable.WITHDRAW_SEND;
        Map<String,String> reqParams = new HashMap<>();
        reqParams.put("requestNo",requestNo);//请求流水号
        reqParams.put("bankCardSerialNo",bankCardSerialNo);//绑定标识
        reqParams.put("withdrawOrderNo",withdrawOrderNo);//提现单号
        reqParams.put("userUid",userUid);//用户标识
        reqParams.put("amt",amt);//金额
        reqParams.put("remark",remark);//备注
        reqParams.put("channelType","YIBAO");//渠道编号，易宝填YIBAO_SUBSTITUTE
        reqParams.put("accountType","PUBLIC");//固定填写PUBLIC

        logger.info("预申请对公提现调用I服务操作");
        //申请账号
        String userName = user.getUsername();

        JSONObject aduitParams = new JSONObject();
        aduitParams.put("userName",userName);//申请账号
        aduitParams.put("processDefineKey","service_pro_con");//服务商对公出金模板名
        aduitParams.put("reason","服务商出金");//备注

        return corporateWithdrawalService.getwithdrawal(url,reqParams,aduitParams);
    }

    //查询我的审批订单
    @RequestMapping("/queryauditlist")
    @RequiresPermissions("corporate:audit:list")
    public R queryAuditList(@RequestParam Map<String,Object> params){
        //查询列表数据
        Query query = new Query(params);

        //获取审核用户名
        SysUserAudit sysUserAudit = sysUserAuditService.queryObject(getUser().getUserId());
        String auditorName = sysUserAudit == null ? null : sysUserAudit.getAuditorName();
        if (StringUtils.isBlank(auditorName)) {
            logger.info("当前登录用户没有对应的审核用户名");
            return null;
        }

        //请求参数
        Map<String, String> reqParams = new HashMap <>();
        reqParams.put("page", (String) params.get("page"));
        reqParams.put("limit", (String) params.get("limit"));
        reqParams.put("processDefineKey", "service_pro_con");  //请求模板
        reqParams.put("userName", auditorName); //审批用户账号

        //查询我的审批订单url
        String queryAuditListUrl = HostsConfig.hostsAudit + ConstantTable.GET_MYAUDIT;
        JSONObject result = corporateWithdrawalService.queryAuditList(queryAuditListUrl, reqParams);
        if (result == null) return R.ok().put("page", new PageUtils(null, 0, query));

        //json数组转化为list
        String jsonArrayStr = result.getJSONObject("data").getString("list");
        List<AuditWithdrawal> AuditWithdrawals =  JSON.parseArray(jsonArrayStr, AuditWithdrawal.class);//JSONUtil.toList(jsonArray, AuditWithdrawal.class);
        int total = AuditWithdrawals == null ? 0 : Integer.valueOf(result.getJSONObject("data").getString("total"));

        PageUtils pageUtil = new PageUtils(AuditWithdrawals, total, query);
        return R.ok().put("page", pageUtil);

    }

    //审批对公提现
    @RequestMapping("/approve")
    @RequiresPermissions("corporate:audit:approve")
    public R approve(@RequestBody Map<String,Object> params){
        logger.info("CorporateWithdrawalController -->审批对公提现入参： {}",params);

        //获取登录用户信息
        SysUserEntity user = getUser();

        //获取审核用户名
        SysUserAudit sysUserAudit = sysUserAuditService.queryObject(user.getUserId());
        String auditorGroupId = sysUserAudit == null ? null : sysUserAudit.getGroupId();
        logger.info("该登录用户的所属审批组别：{}",auditorGroupId);
        if (StringUtils.isBlank(auditorGroupId)) {
            logger.info("当前登录用户没有对应的所属审批组别");
            return R.error("当前登录用户没有对应的所属审批组别");
        }
        /*logger.info("所属机构ID：{}", user.getOrgId());
        if (user.getOrgId() == null){
            return R.error("该用户无归属机构");
        }

        //获取orgId对应的userUid
        List<Map<String, String>> list = corporateBankCardService.getOrgCodes(user.getOrgId());
        String userUid = list.size() == 0 ? null : list.get(0).get("userUid");
        */
        String userUid = params.get("userUid") == null ? null : (String) params.get("userUid");
        if (StringUtils.isBlank(userUid)){
            logger.info("该用户所属机构没有对应的userUid");
            return null;
        }

        //参数校验
        String userName = user.getUsername();
        String withdrawSerialOrderNo = params.get("withdrawSerialOrderNo") == null ? null : (String)params.get("withdrawSerialOrderNo");
        String remark = params.get("remark") == null ? null : (String) params.get("remark");
        String approvalStatus = params.get("approvalStatus") == null ? null : (String) params.get("approvalStatus");//审核结果: true false
        String taskId = params.get("id") == null ? null : (String) params.get("id");
        String traPwd = params.get("traPwd") == null ? null : (String) params.get("traPwd"); //交易密码

        if (StringUtils.isBlank(withdrawSerialOrderNo)
                || StringUtils.isBlank(remark)
                || StringUtils.isBlank(approvalStatus)
                || StringUtils.isBlank(taskId)){
            logger.info("参数验证失败：必要参数缺失");
            return R.error("必要参数缺失");
        }

        //I服务的参数
        Map<String,String> reqParams = new HashMap<>();
        reqParams.put("userUid",userUid);//用户标识
        reqParams.put("withdrawSerialOrderNo",withdrawSerialOrderNo);//提现业务流水号
        reqParams.put("remark",remark);//备注

        //审批服务的参数
        Map<String,String> auditReqParams = new HashMap<>();
        auditReqParams.put("userName",userName);//审核账号
        auditReqParams.put("taskId",taskId);//审核订单id
        auditReqParams.put("result",remark);//备注信息
        auditReqParams.put("approvalStatus",approvalStatus);//审核装填，true 是通过、false拒绝

        //对公提现驳回请求地址
        String rejectUrl = HostsConfig.hostsI + ConstantTable.WITHDRAW_CANCEL;

        //审批服务接口地址
        String aduitUrl = HostsConfig.hostsAudit + ConstantTable.PASS_MYAUDIT;

        logger.info(">>> 开始校验审批密码");
        if (StringUtils.isBlank(user.getTraPwd())){
            logger.info(">>> 审批密码为空,",user.getTraPwd());
            return R.error("未设置审批密码，请设置后，再进行审批");
        }
        if (!checkTraPwdUtils.affirmPwd(traPwd)){
            return R.error("审批密码不正确，请重新输入");
        }
        logger.info(">>> 完成校验审批密码");

        //审核结果为驳回时
        if (StringUtils.equalsIgnoreCase(approvalStatus, "false")){
            logger.info(">>> 对公提现审核驳回");
            return corporateWithdrawalService.rejectwithdrawal(rejectUrl,reqParams,auditReqParams);
        }

        if(!StringUtils.equalsIgnoreCase(AuditGroupEnum.FINANCE_DEP_MANAGER.getDesc(),user.getGroupId())){
            logger.info("审批角色为渠道专员和出纳,{}",user.getGroupId());
            //渠道专员和出纳的审核结果为同意时，只调用审批服务
            return corporateWithdrawalService.getPassMyAudit(aduitUrl,auditReqParams);
        }
        if (StringUtils.equalsIgnoreCase(AuditGroupEnum.FINANCE_DEP_MANAGER.getDesc(),user.getGroupId())){
            logger.info("审批角色为财务经理，{}",user.getGroupId());
            //财务经理的审核结果为同意时，先调用审批服务，再调用I服务
            return corporateWithdrawalService.getPassMyAuditAndPay(aduitUrl,auditReqParams,reqParams);
        }
        /*logger.info("审批角色为财务经理，{}",user.getGroupId());
        //财务经理的审核结果为同意时，先调用审批服务，再调用I服务
        return corporateWithdrawalService.getPassMyAuditAndPay(aduitUrl,auditReqParams,reqParams);*/
        logger.info("审批角色异常，user.getGroupId():{}",user.getGroupId());
        return R.error("审批角色异常,审批失败");
    }

}



