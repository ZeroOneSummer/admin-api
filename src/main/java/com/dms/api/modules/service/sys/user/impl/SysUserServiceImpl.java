package com.dms.api.modules.service.sys.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dms.api.common.utils.HttpClientUtil;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.RedisService;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.config.SmsConfig;
import com.dms.api.modules.dao.sys.user.SysUserDao;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.sys.role.SysRoleService;
import com.dms.api.modules.service.sys.user.SysUserRoleService;
import com.dms.api.modules.service.sys.user.SysUserService;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.common.exception.DMException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 系统用户
 * @author
 * @email @danpacmall.com
 * @date 2016年9月18日 上午9:46:09
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {
    Logger logger = LoggerFactory.getLogger(SysUserServiceImpl.class);

    private String userInfoCKey = ConstantTable.USER_INFO_REDIS_KEY_PREFIX;

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private RedisService redisService;

    @Override
    public List<String> queryAllPerms(Long userId) {
        return sysUserDao.queryAllPerms(userId);
    }

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return sysUserDao.queryAllMenuId(userId);
    }

    @Override
    public SysUserEntity queryByUserName(String username) {
        return sysUserDao.queryByUserName(username);
    }

    @Override
    public SysUserEntity queryObject(Long userId) {
        SysUserEntity sysUserEntity = null;//redisService.get(userInfoCKey + userId, SysUserEntity.class);
        if (null == sysUserEntity) {
            sysUserEntity = sysUserDao.queryObject(userId);
            redisService.set(userInfoCKey + userId, sysUserEntity, -1);
        }
        return sysUserEntity;
    }

    @Override
    public List<SysUserEntity> queryList(Map<String, Object> map) {
        return sysUserDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return sysUserDao.queryTotal(map);
    }

    @Override
    @Transactional
    public void save(SysUserEntity user) {
        user.setCreateTime(new Date());
        //sha256加密
        String salt = StringUtils.UUIDStrNoSplit();
        user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
        user.setSalt(salt);
        if(!StringUtils.isBlank(user.getTraPwd())){
            user.setTraPwd(new Sha256Hash(user.getTraPwd(), salt).toHex());
        }

        sysUserDao.save(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void update(SysUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
        }
        redisService.del(userInfoCKey + user.getUserId());
        sysUserDao.update(user);

        //检查角色是否越权
        checkRole(user);

        //保存用户与角色关系
        sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional
    public void deleteBatch(Long[] userId) {
        Long[] cKeys = new Long[userId.length];
        int i = 0;
        for (Long l : userId) {
            cKeys[i] = l;
            i++;
        }
        redisService.del(cKeys);
        sysUserDao.deleteBatch(userId);
    }

    @Override
    public int updatePassword(Long userId, String password, String newPassword) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("password", password);
        map.put("newPassword", newPassword);
        redisService.del(userInfoCKey + userId);
        return sysUserDao.updatePassword(map);
    }

    /**
     * 检查角色是否越权
     */
    private void checkRole(SysUserEntity user) {
        //如果不是超级管理员，则需要判断用户的角色是否自己创建
        SysUserEntity createUser = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
        if (createUser.isSuperAdmin()) {
            return;
        }

        //查询用户创建的角色列表
        List<Long> roleIdList = sysRoleService.queryRoleIdList(user.getCreateUserId());

        //判断是否越权
        if (!roleIdList.containsAll(user.getRoleIdList())) {
            throw new DMException("新增用户所选角色，不是本人创建");
        }
    }

    /**
     *设置审批密码（仅限管理员）
     */
    @Override
    public R addOrUpdatePwd(SysUserEntity user) {
        //根据userId查询数据库中对象
        SysUserEntity userEntity = sysUserDao.queryObject(user.getUserId());
        //入参实体
        SysUserEntity entity = new SysUserEntity();
        entity.setUserId(user.getUserId());
        entity.setTraPwd(new Sha256Hash(user.getNewTraPwd(), userEntity.getSalt()).toHex());

        if (!StringUtils.isBlank(userEntity.getTraPwd())){ //有审批密码，核对原密码，修改
            String traPwd = new Sha256Hash(user.getTraPwd(), userEntity.getSalt()).toHex();
            if(!traPwd.equals(userEntity.getTraPwd())){
                logger.info("审批密码错误: 原密码 {} 输入密码 {}", user.getTraPwd(), traPwd);
                return R.error("原密码错误");
            }
        }

        try {
            sysUserDao.update(entity);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("审批密码设置异常: ", e);
            return R.error("密码设置异常");
        }
    }

    /**
     * 设置审批密码（仅限服务商）
     */
    @Override
    public R sendOrAckSms(SysUserEntity user) {
        logger.info(">>>>>sendOrAckSms短信接口：参数：{}", user);

        String smsCode = user.getSmsCode(); //验证码
        String phone = user.getMobile(); //手机号
        String smsAppKey = SmsConfig.smsAppKey;  //应用秘钥【配置】
        String channelNo = SmsConfig.channelNo; //短信模板号【配置】
        String sign = SmsConfig.smsSign; //短信接口盐值【配置】
        String url = StringUtils.isBlank(smsCode) ?  SmsConfig.smsIp + "/sendSms" : SmsConfig.smsIp + "/verifyCode";
        JSONObject params = new JSONObject();
        String traPwd = null;

        //根据userId查询数据库中对象
        SysUserEntity userEntity = sysUserDao.queryObject(user.getUserId());

        //公共参数验证
        if (StringUtils.isBlank(url)
                || StringUtils.isBlank(smsAppKey)
                || StringUtils.isBlank(phone)) {
            logger.info("必要参数缺失");
            return R.error("必要参数缺失");
        }

        if (StringUtils.isBlank(smsCode)) {
            //无验证码，send请求
            if (StringUtils.isBlank(channelNo)
                    || StringUtils.isBlank(sign)) {
                logger.info("必要参数缺失channelNo or sign");
                return R.error("必要参数缺失");
            }
            String code = String.valueOf(new Random().nextInt(9000) + 1000);
            params.put("templateType", "0");
            params.put("channelNo", channelNo);
            params.put("sign", sign);
            params.put("isTiming", 0);
            params.put("definiteTime", "0");
            params.put("params", "{'code': " + code + "}");
        } else {
            if(!StringUtils.isBlank(userEntity.getTraPwd())){
                //传输对象的审批密码
                traPwd = new Sha256Hash(user.getTraPwd(), userEntity.getSalt()).toHex();
                //比较传输对象的审批密码与数据库的审批密码
                if(!traPwd.equals(userEntity.getTraPwd())){
                    logger.info("审批密码错误: 原密码 {} 输入密码 {}", user.getTraPwd(), traPwd);
                    return R.error("原审批密码错误");
                }
            }
            //确认验证码，ack请求
            params.put("code", smsCode);
        }
        //公共参数封装
        params.put("phone", phone);
        params.put("messageType", "0");
        params.put("applicationKey", smsAppKey);
        params.put("validTime", 5);

        //开始调用接口

        try {
            logger.info(">>>>>请求url：{} 参数：{}", url, params);
            String response = HttpClientUtil.doPostJson(url, JSON.toJSONString(params), 30000);
            JSONObject rs = JSON.parseObject(response);
            if (!StringUtils.equalsIgnoreCase(rs.getString("rc"), "200")){
                if (StringUtils.equalsIgnoreCase(rs.getString("rc"), "-3")){
                    logger.info("请求失败：{}", rs);
                    return R.error((int)rs.get("rc"), rs.getString("msg"));
                }
                logger.info("请求失败：{}", rs);
                return R.error("请求失败" + rs.getString("msg"));
            }
            logger.info("请求成功：{}", rs);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("请求异常：", e);
        }
        logger.info(">>>>>sendOrAckSms短信接口调用结束");


        //ack请求时
        if (!StringUtils.isBlank(smsCode)) {
            SysUserEntity entity = new SysUserEntity();
            entity.setUserId(user.getUserId());
            entity.setTraPwd(new Sha256Hash(user.getNewTraPwd(), userEntity.getSalt()).toHex());

            try {
                logger.info(">>>>>开始更新DB");
                sysUserDao.update(entity);
                logger.info(">>>>>更新完成");
            } catch (Exception e) {
                e.printStackTrace();
                logger.info("更新失败：{}", e);
                return R.error("操作失败");
            }
        }

        return R.ok();
    }

    @Override
    public int resetAuditPwd(SysUserEntity user) {
        return sysUserDao.update(user);
    }

}
