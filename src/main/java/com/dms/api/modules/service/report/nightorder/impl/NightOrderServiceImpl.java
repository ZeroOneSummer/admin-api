package com.dms.api.modules.service.report.nightorder.impl;

import com.dms.api.modules.dao.report.nightorder.NightOrderDao;
import com.dms.api.modules.entity.report.nightorder.NightOrderEntity;
import com.dms.api.modules.service.report.nightorder.NightOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NightOrderServiceImpl implements NightOrderService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    NightOrderDao nightOrderDao;

    @Override
    public List <NightOrderEntity> queryList(Map<String, Object> map) {
        return nightOrderDao.queryList(map);
    }
}
