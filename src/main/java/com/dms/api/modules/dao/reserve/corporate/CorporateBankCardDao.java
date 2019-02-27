package com.dms.api.modules.dao.reserve.corporate;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.corporate.CorporateBankCardEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface CorporateBankCardDao extends BaseDao<CorporateBankCardEntity> {

    //获取用户的信息
    List<Map<String, String>> getOrgCodes(@Param("orgId") Long orgId);

    //获取银行卡编码列表
    @Select("SELECT scp.id AS id,scp.val AS bankName,scp.`key` AS bankCode,scp.aux_val AS bankBmcode FROM sys_cast_params scp WHERE scp.groupid = 'BANK_ENUM';")
    List<Map<String,String>> getBankCodes();

}