package com.dms.api.modules.service.mall.user;

import com.dms.api.modules.entity.mall.user.ModificationPhoneNumEntity;

import java.util.List;
import java.util.Map;

public interface ModificationPhoneNumService {

    List<ModificationPhoneNumEntity> queryList(Map <String, Object> map);

    int queryTotal(Map <String, Object> map);
}
