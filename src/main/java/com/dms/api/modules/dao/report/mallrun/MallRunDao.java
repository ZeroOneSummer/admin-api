package com.dms.api.modules.dao.report.mallrun;


import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.report.mallrun.MallRunEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MallRunDao extends BaseDao<MallRunEntity> {

    //1.按年统计单个服务商 countType='Y'，dealerId!=-1，dataType='null'
    List<MallRunEntity> mallRunCount1(Map<String, Object> map);

    //2.按年统计所有服务商 countType='Y'，dealerId=-1，dataType='null'
    List<MallRunEntity> mallRunCount2(Map<String, Object> map);

    //3.按月统计单个服务商单日 countType='M'，dealerId!=-1，dataType='SIG'
    List<MallRunEntity> mallRunCount3(Map<String, Object> map);

    //4.按月统计单个服务商累计 countType='M'，dealerId!=-1，dataType='ADD'
    List<MallRunEntity> mallRunCount4(Map<String, Object> map);

    //5.按月统计所有服务商单日 countType='M'，dealerId=-1，dataType='SIG'
    List<MallRunEntity> mallRunCount5(Map<String, Object> map);

    //6.按月统计所有服务商累计 countType='M'，dealerId=-1，dataType='ADD'
    List<MallRunEntity> mallRunCount6(Map<String, Object> map);

    //查询服务商列表
    List<Map<String, Object>> queryDealerList(Map<String, Object> map);

}