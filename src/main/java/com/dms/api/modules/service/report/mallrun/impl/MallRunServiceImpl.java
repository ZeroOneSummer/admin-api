package com.dms.api.modules.service.report.mallrun.impl;


import com.dms.api.modules.dao.report.mallrun.MallRunDao;
import com.dms.api.modules.entity.report.mallrun.MallRunEntity;
import com.dms.api.modules.service.report.mallrun.MallRunService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MallRunServiceImpl implements MallRunService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MallRunDao mallRunDao;

    @Override
    public List<MallRunEntity> mallRunCount1(Map<String, Object> map) {
        return mallRunDao.mallRunCount1(map);
    }

    @Override
    public List <MallRunEntity> mallRunCount2(Map <String, Object> map) {
        return mallRunDao.mallRunCount2(map);
    }

    @Override
    public List <MallRunEntity> mallRunCount3(Map <String, Object> map) {
        return mallRunDao.mallRunCount3(map);
    }

    @Override
    public List <MallRunEntity> mallRunCount4(Map <String, Object> map) {
        return mallRunDao.mallRunCount4(map);
    }

    @Override
    public List <MallRunEntity> mallRunCount5(Map <String, Object> map) {
        return mallRunDao.mallRunCount5(map);
    }

    @Override
    public List <MallRunEntity> mallRunCount6(Map <String, Object> map) {
        return mallRunDao.mallRunCount6(map);
    }

    @Override
    public List <Map <String, Object>> queryDealerList(Map <String, Object> map) {
        return mallRunDao.queryDealerList(map);
    }

}
