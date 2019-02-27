package com.dms.api.modules.service.basis.org;

import com.dms.api.modules.entity.basis.org.BmDealerInfo;
import com.dms.api.modules.entity.basis.org.TempEntity;

import java.util.List;
import java.util.Map;
/**
 * @author 40
 * date 2017/10/25
 * time 15:15
 * decription:
 */
public interface BmDealerInfoService {

    List<BmDealerInfo> queryList(Map <String, Object> map);

    Integer queryTotal(Map <String, Object> map);

    BmDealerInfo queryObject(Long dealerId);

    List<BmDealerInfo> getDealers(Long orgId);

    List<BmDealerInfo> getDealerInfos(Map map);

    List<TempEntity> getByOrgIDInfos(Map map);
}
