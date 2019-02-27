package com.dms.api.common.utils;

import com.alibaba.fastjson.JSON;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.sys.user.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统用户审批密码验证工具类
 */
@Component
public class CheckTraPwdUtils {
    static Logger logger = LoggerFactory.getLogger(CheckTraPwdUtils.class);

    @Autowired
    private SysUserService sysUserService;

    /**
     * 审批密码确认
     * @return boolean
     */
    public boolean affirmPwd(String traPwd){
        //获取用户信息
        SysUserEntity user = (SysUserEntity)SecurityUtils.getSubject().getPrincipal();
        //根据useId查询数据库中对象
        SysUserEntity userEntity = sysUserService.queryObject(user.getUserId());
        logger.info(">>> 查询当前登录用户信息：{}", JSON.toJSONString(userEntity));

        if (StringUtils.isBlank(userEntity.getTraPwd())){
            logger.info("该用户暂无审批密码");
            return false;
        }
        traPwd = new Sha256Hash(traPwd, userEntity.getSalt()).toHex(); //加密

        return StringUtils.equalsIgnoreCase(traPwd, userEntity.getTraPwd());
    }

}
