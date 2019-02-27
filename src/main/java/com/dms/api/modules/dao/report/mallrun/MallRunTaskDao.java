package com.dms.api.modules.dao.report.mallrun;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.report.mallrun.MallRunEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MallRunTaskDao extends BaseDao<MallRunEntity> {

    //查询服务商、类型分组列表
    List<MallRunEntity> queryTypeGroups();

    //生成结算表数据（综合）  传参：creType[D-日，M-月]，creWay[CURR-当前，ALL-所有]
    int createSettlementData(Map<String, Object> map);

    //生成结算累计表数据（综合）  传参：timeType[D-日，M-月]、type、dealerCode, creWay[CURR-当前，ALL-所有]
    int createSettlementDataTotal(Map<String, Object> map);

    //清空结算表数据（所有）
    int delSettlementData();

    //清空结算累计表数据（所有）
    int delSettlementDataTotal();

}