package com.dms.api.modules.service.basis.org.impl;

import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.dao.basis.org.BmDealerInfoDao;
import com.dms.api.modules.entity.basis.org.BmDealerInfo;
import com.dms.api.modules.entity.basis.org.TempEntity;
import com.dms.api.modules.service.basis.org.BmDealerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;

/**
 * @author 40
 * date 2017/10/25
 * time 15:16
 * decription:
 */
@Service
public class BmDealerInfoServiceImpl implements BmDealerInfoService {

    @Autowired
    BmDealerInfoDao bmDealerInfoDao;

    @Override
    public List<BmDealerInfo> queryList(Map<String, Object> map) {
        return bmDealerInfoDao.queryList(map);
    }

    @Override
    public Integer queryTotal(Map<String, Object> map) {
        return bmDealerInfoDao.queryTotal(map);
    }

    @Override
    public BmDealerInfo queryObject(Long dealerId) {
        return bmDealerInfoDao.selectByPrimaryKey(dealerId);
    }

    @Override
    public List<BmDealerInfo> getDealers(Long dealerId) {
        return bmDealerInfoDao.getDealers(dealerId);
    }

    @Override
    public List<BmDealerInfo> getDealerInfos(Map map) {
        List dealerInfos = bmDealerInfoDao.getDealerInfos(map);
        listSort(dealerInfos,"asc");
        return dealerInfos;
    }

    @Override
    public List<TempEntity> getByOrgIDInfos(Map map) {
        return bmDealerInfoDao.getByOrgIDInfos(map);
    }

    /**
     * 排序，默认升序
     * @param resultList
     * @param sort
     */
    private  void listSort(List<BmDealerInfo> resultList, String sort) {
            Collections.sort(resultList, new Comparator<BmDealerInfo>() {
                @Override
                public int compare(BmDealerInfo o1, BmDealerInfo o2) {
                    String name1 = o1.getDealerCode();
                    String name2 = o2.getDealerCode();
                    Collator instance = Collator.getInstance(Locale.CHINA);
                    if (StringUtils.isBlank(sort)) {
                        return instance.compare(name1, name2);
                    } else if ("DESC".equals(sort.toUpperCase())) {
                        return instance.compare(name2, name1);
                    } else {
                        return instance.compare(name1, name2);
                    }

                }
            });
        }
}
