package com.dms.api.modules.dao.report.nightorder;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.report.nightorder.NightOrderEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface NightOrderDao extends BaseDao<NightOrderEntity> {

}