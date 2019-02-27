package com.dms.api.modules.service.report.mallrun.impl;

import com.alibaba.fastjson.JSON;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.dao.report.mallrun.MallRunTaskDao;
import com.dms.api.modules.entity.report.mallrun.MallRunEntity;
import com.dms.api.modules.service.report.mallrun.MallRunTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MallRunTaskServiceImpl implements MallRunTaskService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MallRunTaskDao mallRunTaskDao;

    @Async
    @Transactional
    @Override
    public void createSettlementData(Map<String, Object> map) {

        logger.info("入参map:{}", map);
        if (StringUtils.equalsIgnoreCase("ALL", (String)map.get("creWay"))){
            logger.info("生成全部结算数据，开始删除原数据...");
            mallRunTaskDao.delSettlementData();
            logger.info("删除原数据已完成");
        }

        logger.info("开始生成结算数据...");
        mallRunTaskDao.createSettlementData(map);
        logger.info("生成结算数据已完成");

    }

    @Async
    @Transactional
    @Override
    public void createSettlementDataTotal(Map <String, Object> map) {

        //生成类型creWay[CURR-当前，ALL-所有]
        String creWay = (String)map.get("creWay");

        logger.info("入参map:{}", map);
        if (StringUtils.equalsIgnoreCase("ALL", creWay)){
            logger.info("生成全部累计结算数据，开始删除原数据...");
            mallRunTaskDao.delSettlementDataTotal();
            logger.info("删除原数据已完成");
        }

        List <MallRunEntity> list = mallRunTaskDao.queryTypeGroups();
        logger.info("查询服务商、类型分组列表结果：{}", JSON.toJSONString(list));
        if (list == null ){
            throw new RuntimeException("结果为null，手动抛异常回滚事务");
        }

        //timeType[D-日，M-月]、type、dealerCode, creWay[CURR-当前，ALL-所有]
        logger.info("开始循环生成累计结算数据");
        list.stream().forEach(entity -> {
            Map<String, Object> parmas = new HashMap <>();
            parmas.put("type", entity.getType());
            parmas.put("dealerCode", entity.getDealerCode());
            parmas.put("creWay", creWay);
            //日累计
            parmas.put("timeType", "Day");
            mallRunTaskDao.createSettlementDataTotal(parmas);
            //月累计
            parmas.put("timeType", "Mon");
            mallRunTaskDao.createSettlementDataTotal(parmas);
        });
        logger.info("循环生成累计结算数据已完成");

    }

}
