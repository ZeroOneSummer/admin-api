package com.dms.api.modules.dao.mall.order.info;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.mall.order.info.BmUserAccountEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 资金账户表-----B系统表名为：user_Account
 */
@Mapper
public interface BmUserAccountDao extends BaseDao<BmUserAccountEntity> {

    List<BmUserAccountEntity> queryListForDealer(Map <String, Object> map);

    List<BmUserAccountEntity> queryListByAccId(Map <String, Object> map);
}
