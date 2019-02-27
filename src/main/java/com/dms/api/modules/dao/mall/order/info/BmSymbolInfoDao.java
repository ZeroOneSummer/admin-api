package com.dms.api.modules.dao.mall.order.info;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.mall.order.info.BmSymbolInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 商品信息-----B系统表名为：symbol_Info
 */
@Mapper
public interface BmSymbolInfoDao extends BaseDao<BmSymbolInfoEntity> {

    List<BmSymbolInfoEntity> queryListByIds(Map <String, Object> map);
}
