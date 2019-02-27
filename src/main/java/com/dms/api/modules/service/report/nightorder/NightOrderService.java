package com.dms.api.modules.service.report.nightorder;

import com.dms.api.modules.entity.report.nightorder.NightOrderEntity;

import java.util.List;
import java.util.Map;

public interface NightOrderService {

    List<NightOrderEntity> queryList(Map<String, Object> map);
}
