package com.dms.api.modules.controller.mall.order.repo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.HostsConfig;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.core.util.ConvertUtil;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.mall.order.repo.AuditOrder;
import com.dms.api.modules.entity.mall.order.repo.OrderDetail;
import com.dms.api.modules.entity.sys.user.SysUserAudit;
import com.dms.api.modules.service.mall.order.repo.AuditOrderService;
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

@RequestMapping("/auditOrder")
@RestController
public class AuditOrderController extends AbstractController {

    @Autowired
    ConvertUtil convertUtil; //sign加密

    @Autowired
    AuditOrderService auditOrderService;

    @Autowired
    SysUserAuditService sysUserAuditService;


    @RequestMapping("/list")
    @RequiresPermissions("auditOrder:list")
    public R list(@RequestParam Map<String, Object> params) {
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
        reqParams.put("processDefineKey", "repurchase_application");  //请求模板
        reqParams.put("userName", auditorName); //审批用户账号

        JSONObject result = auditOrderService.queryList(HostsConfig.hostsAudit + ConstantTable.GET_MYAUDIT, reqParams);
        if (result == null) return R.ok().put("page", new PageUtils(null, 0, query));

        //json数组转化为list
        String jsonArrayStr = result.getJSONObject("data").getString("list");
        List<AuditOrder> AuditOrders =  JSON.parseArray(jsonArrayStr, AuditOrder.class);//JSONUtil.toList(jsonArray, AuditOrder.class);
        int total = AuditOrders == null ? 0 : Integer.valueOf(result.getJSONObject("data").getString("total"));

        PageUtils pageUtil = new PageUtils(AuditOrders, total, query);
        return R.ok().put("page", pageUtil);
    }

    //查询当前登录用户信息
    @RequestMapping("/sysUser")
    public R getSysUserInfo() {
        logger.info("获取当前登录用户信息");
        SysUserAudit sysUserAudit = sysUserAuditService.queryObject(getUser().getUserId());
        logger.info("当前登录用户信息：{}", JSON.toJSONString(sysUserAudit));
        return R.ok().put("sysUserAudit", sysUserAudit);
    }

    //审批接口
    @RequestMapping("/check")
    @RequiresPermissions("auditOrder:check")
    public R addSysUserInfo(@RequestBody Map<String, String> params) {
        logger.info("审批接口auditOrder-check入参：{}", params);

        //根据logincode查询bm_user_infoBrief对应的pay_id(签约席位号)
        String loginCode = params.get("loginCode") == null ? null : (String) params.get("loginCode");
        if (StringUtils.isBlank(loginCode)) return R.error("必要参数缺失");
        String userUid = sysUserAuditService.queryPayId(loginCode);
        logger.info("根据logincode {} 查询对应 签约席位号 {}", loginCode, userUid);

        //参数校验
        String userName = params.get("userName") == null ? null : (String) params.get("userName");
        String taskId = params.get("taskId") == null ? null : (String) params.get("taskId");
        String result = params.get("result") == null ? "" : (String) params.get("result");
        String approvalStatus = params.get("approvalStatus") == null ? null : (String) params.get("approvalStatus");
        String payTYpe = params.get("payTYpe") == null ? "" : (String) params.get("payTYpe");

        //划转相关参数
        String amt = params.get("amt") == null ? null : (String) params.get("amt");
        String requestNo = params.get("requestNo") == null ? null : (String) params.get("requestNo");
        String orderNo = params.get("orderNo") == null ? null : (String) params.get("orderNo");
        String productName = params.get("productName") == null ? null : (String) params.get("productName");
        String type = params.get("type") == null ? null : (String) params.get("type");  //请求类型：apply-回购请求，ack-回购请求确认
        String smsCode = params.get("smsCode") == null ? null : (String) params.get("smsCode"); //回购确认短信验证码
        String applyBankSerialOrderNo = params.get("applyBankSerialOrderNo") == null ? null : (String) params.get("applyBankSerialOrderNo"); //银行流水订单号
        String mall = params.get("mall") == null ? null : (String) params.get("mall"); //商城名称
        String smsStatus = params.get("smsStatus") == null ? null : (String) params.get("smsStatus"); //确认方式：'ACTIVE'-验证码确认，否则交易密码确认
        String traPwd = params.get("traPwd") == null ? null : (String) params.get("traPwd"); //交易密码

        if (StringUtils.isBlank(userName)
                || StringUtils.isBlank(taskId)
                || StringUtils.isBlank(approvalStatus)
                || StringUtils.isBlank(amt)
                /*|| StringUtils.isBlank(userUid)*/
                || StringUtils.isBlank(requestNo)
                || StringUtils.isBlank(orderNo)
                /*|| StringUtils.isBlank(productName)*/
                || StringUtils.isBlank(payTYpe)
                || StringUtils.isBlank(type)
                || StringUtils.isBlank(mall)
                || StringUtils.equalsIgnoreCase(mall, "null")
            ){

            logger.info("参数验证失败：必要参数缺失");
            return R.error("必要参数缺失");
        }

        //请求地址&参数
        String url = HostsConfig.hostsAudit + ConstantTable.PASS_MYAUDIT;
        Map<String, String> reqParams = new HashMap <>();
        reqParams.put("userName", userName); //审批账号
        reqParams.put("taskId", taskId); //订单ID
        reqParams.put("result", result); //备注信息
        reqParams.put("approvalStatus", approvalStatus); //审核意见 true/false

        reqParams.put("type", type); //apply : 回购请求; ack : 回购请求确认
        reqParams.put("amt", amt); //回购金额
        reqParams.put("userUid", userUid); //签约席位号(数据库查询)
        reqParams.put("requestNo", requestNo); //请求编号
        reqParams.put("orderNo", orderNo); //订单号
        reqParams.put("productName", "回购商品"); //产品名称
        reqParams.put("payTYpe", payTYpe); //支付类型（线下支付无需调用划账接口）
        reqParams.put("smsCode", smsCode); //回购确认短信验证码
        reqParams.put("applyBankSerialOrderNo", applyBankSerialOrderNo); //银行流水订单号
        reqParams.put("mall", mall); //商城名称
        reqParams.put("smsStatus", smsStatus); //确认方式
        reqParams.put("traPwd", traPwd); //交易密码

        //回购短信参数
        Map<String, String> smsParams = new HashMap <>();
        smsParams.putAll(reqParams);
        smsParams.put("productName", productName); //产品名称
        smsParams.remove("traPwd");
        smsParams.remove("mall");
        smsParams.remove("key");
        //调审核接口
        try {
            //审核结果同步到A系统
            JSONObject syncParams = new JSONObject();
            String status = StringUtils.equalsIgnoreCase(approvalStatus, "true") ? "1" : "0";
            syncParams.put("buyBackNumber", requestNo); //回购单号
            syncParams.put("result", status); //审核结果: 1-true  0-false
            syncParams.put("remark", result); //备注
            String sign = convertUtil.crypt(syncParams);
            syncParams.put("sign",sign);
            syncParams.put("mall", mall); //商城名称
            //驳回状态直接调审核接口，不划账
            if(StringUtils.equalsIgnoreCase(approvalStatus, "false")){
                logger.info(">>> 审核驳回");
                reqParams.put("payTYpe", "OFFLINE"); //线下不划账
                smsParams.put("payTYpe", "OFFLINE"); //驳回不发短信
                return auditOrderService.check(url, reqParams, syncParams, smsParams, loginCode);
            }
            if (StringUtils.equalsIgnoreCase(type, "apply") && StringUtils.equalsIgnoreCase(payTYpe, "ONLINEL")){
                //申请回购，发送验证码和获取银行流水订单号
                logger.info(">>> 申请回购，获取验证码或确认交易密码开关，请求type: {} 确认类型：{}", type, smsStatus);
                return auditOrderService.sendSmsCode(url, reqParams, syncParams);
            }else if(StringUtils.equalsIgnoreCase(type, "ack")){
                //回购确认，资金划转
                logger.info(">>> 回购确认，资金划转，请求type: {} 确认类型：{}", type, smsStatus);
                return auditOrderService.check(url, reqParams, syncParams, smsParams, loginCode);
            }else{
                logger.info(">>> 未知请求，请求type: {} 确认类型：{}", type, smsStatus);
                return  R.error("操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("我的审批订单审批接口-异常：{}", e);
            return R.error("操作失败");
        }

    }


    //订单详情
    @RequestMapping("/detail")
    public OrderDetail getOrderDetail(@RequestParam String subNumber, @RequestParam String mall) {
        logger.info("获取订单详情接口auditOrder-detail入参：subNumber: {} mall: {}", subNumber, mall);

        if (StringUtils.isBlank(subNumber)
                || StringUtils.isBlank(mall)
                || "null".equals(mall)){
            logger.info("参数验证失败：必要参数缺失");
            return null;
        }

        //请求地址&参数
        String ip = StringUtils.equalsIgnoreCase(mall, "KKG") ? HostsConfig.hostsI : HostsConfig.hostsIA;
        String url = ip + ConstantTable.GET_ORDER;
        Map<String, String> reqParams = new HashMap <>();
        reqParams.put("subNumber", subNumber); //订单号

        //调用订单详情接口
        try {
            JSONObject jsonObject = auditOrderService.getOrderDetail(url, reqParams);
            OrderDetail orderDetail = JSON.parseObject(jsonObject.getJSONObject("data").getJSONArray("list").get(0).toString(), OrderDetail.class);
            return orderDetail;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("获取订单详情接口-异常：{}", e);
            return null;
        }
    }


}
