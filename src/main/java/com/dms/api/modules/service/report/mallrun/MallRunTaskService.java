package com.dms.api.modules.service.report.mallrun;

import java.util.Map;

public interface MallRunTaskService {

    //生成结算表数据（综合）  传参：creType[D-日，M-月]，creWay[CURR-当前，ALL-所有]
    void createSettlementData(Map<String, Object> map);

    //生成结算累计表数据（综合）  传参：timeType[D-日，M-月]、type、dealerCode, creWay[CURR-当前，ALL-所有]
    void createSettlementDataTotal(Map<String, Object> map);

}
