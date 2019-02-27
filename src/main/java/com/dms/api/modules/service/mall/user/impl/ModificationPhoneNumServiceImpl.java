package com.dms.api.modules.service.mall.user.impl;

import com.dms.api.modules.controller.mall.user.ModificationPhoneNumController;
import com.dms.api.modules.dao.mall.user.ModificationPhoneNumDao;
import com.dms.api.modules.entity.mall.user.ModificationPhoneNumEntity;
import com.dms.api.modules.service.mall.user.ModificationPhoneNumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ModificationPhoneNumServiceImpl implements ModificationPhoneNumService {
    Logger logger = LoggerFactory.getLogger(ModificationPhoneNumController.class);

    @Autowired
    ModificationPhoneNumDao modificationPhoneNumDao;

    @Override
    public List<ModificationPhoneNumEntity> queryList(Map<String, Object> map) {
        return modificationPhoneNumDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return modificationPhoneNumDao.queryTotal(map);
    }
}
