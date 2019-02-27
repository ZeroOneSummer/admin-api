package com.dms.api.modules.service.basis.org.impl;


import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.dao.basis.org.BmOrganizationDao;
import com.dms.api.modules.dao.basis.org.BmSysOrganizationDao;
import com.dms.api.modules.entity.basis.org.BmOrganizationEntity;
import com.dms.api.modules.entity.basis.org.BmSysOrganization;
import com.dms.api.modules.entity.basis.org.BmSysOrganizationWithParent;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.basis.org.BmOrganizationService;
import com.dms.api.modules.service.sys.config.SysConfigService;
import com.dms.api.modules.service.sys.user.SysUserOrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("bmOrganizationService")
public class BmOrganizationServiceImpl implements BmOrganizationService {
    @Autowired
    private BmOrganizationDao bmOrganizationDao;

    @Autowired
    private BmSysOrganizationDao bmSysOrganizationDao;

    @Autowired
    private SysConfigService sysConfigService;


    @Override
    public BmOrganizationEntity queryObject(Long id) {
        return bmOrganizationDao.queryObject(id);
    }

    @Override
    public List<BmOrganizationEntity> queryList(Map<String, Object> map) {
        return bmOrganizationDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return bmOrganizationDao.queryTotal(map);
    }

    @Override
    public void save(BmOrganizationEntity bmOrganization) {
        bmOrganizationDao.save(bmOrganization);
    }

    @Override
    public void update(BmOrganizationEntity bmOrganization) {
        bmOrganizationDao.update(bmOrganization);
    }

    @Override
    public void delete(Long id) {
        bmOrganizationDao.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        bmOrganizationDao.deleteBatch(ids);
    }

    @Override
    public BmOrganizationEntity queryObjetByUserId(Long id) {

        return null;
    }


    /**
     * 根据用户信息获取其所拥有的机构树
     */
//    @Override
//    public List<BmOrganizationEntity> queryListByUser(SysUserEntity user) {
//        //查询出所有的机构
////        List<BmOrganizationEntity> allOrgList = bmOrganizationDao.queryListByOrg(null);
//        List<BmOrganizationEntity> allOrgList = bmOrganizationDao.queryListByOrg(null);
//        //判断是否为超管，如果不是则筛选出其权限所能查看的机构信息
//        if (user.isSuperAdmin()) {
//            return allOrgList;
//        } else {
//            List<BmOrganizationEntity> userOrgList = new ArrayList<>();
//            //获取用户对应机构
//            allOrgList.parallelStream()
//                    .filter(org -> org.getOrgId() == user.getOrgId())
//                    .findFirst()
//                    .ifPresent(org -> userOrgList.add(org));
//            //如果用户所属机构不存在则返回null
//            if (userOrgList.size() == 0) {
//                return null;
//            }
//            //获取用户对应机构的子机构
//            filterChild(user.getOrgId(), userOrgList, allOrgList);
//            return userOrgList;
//        }
//    }

    /**
     * 根据用户信息获取其所拥有的机构树
     */
    @Override
    public List<BmSysOrganization> queryListByUser(SysUserEntity user) {
        //查询出所有的机构
        List<BmSysOrganization> allOrgList = bmSysOrganizationDao.queryListByOrg(null);
        //判断是否为超管，如果不是则筛选出其权限所能查看的机构信息
        if (user.isSuperAdmin()) {
            return allOrgList;
        } else {
            //获取用户对应机构
            List<BmSysOrganization> userOrgList = new ArrayList<>();
            if (null != user.getDealerId()) {
                userOrgList.addAll(allOrgList.parallelStream()
                        .filter(org -> org.getBrokerDealerId().equals(user.getDealerId()+""))
                        .collect(Collectors.toList()));
            }
            if (null != user.getOrgId()) {
                //获取用户对应机构
                allOrgList.parallelStream()
                        .filter(org -> Long.parseLong(org.getOrgId()) == user.getOrgId())
                        .findFirst()
                        .ifPresent(org -> userOrgList.add(org));
                //如果用户所属机构不存在则返回null
                if (userOrgList.size() == 0) {
                    return userOrgList;
                }
                //获取用户对应机构的子机构
                filterChild(user.getOrgId(), userOrgList, allOrgList);
            }
            return userOrgList;
        }
    }

    /**
     * 筛选出子机构
     */
    private void filterChild(Long pid, List<BmSysOrganization> userOrgList, List<BmSysOrganization> allOrgList) {
        allOrgList.parallelStream()
                .filter(org -> Long.parseLong(org.getParentId()) == pid && !org.getOrgId().equals(org.getParentId()))
                .forEach(org -> {
                    userOrgList.add(org);
                    filterChild(Long.parseLong(org.getOrgId()), userOrgList, allOrgList);
                });
    }

    @Override
    public List<BmSysOrganization> queryListInLevel(Query queryParam) {
        createCondition(queryParam);
        Integer levelCount = (Integer) queryParam.get("levelCount");

        if (0 == levelCount){
            //没有下属子孙机构
            return new ArrayList<BmSysOrganization>();
        }else {
            return bmSysOrganizationDao.queryListInLevel(queryParam);
        }

        /*//获取机构层数
        String valueStr = sysConfigService.getValue(ConstantTable.CONFIG_ORGLEVELCOUNT);
        int levelCount = Integer.parseInt(valueStr);

        if (0 == levelCount){
            //没有下属子孙机构
            return new ArrayList<BmSysOrganization>();
        } else {


            //所有子孙机构视图表名
            List<String > levelViewList = new ArrayList<>(levelCount);

            String level = (String)queryParam.get("level");
            if (StringUtils.isBlank(level) || "0" .equals(level)){
                //获取所有子孙机构

                //构建所有子孙机构视图表名
                for (int i = levelCount; i >0; i --){
                    levelViewList.add(ConstantTable.ORG_LEVEL_VIEW_PREFIX + i);
                }

            } else {
                //获取指定层别机构

                //构建指定层别机构视图名
                levelViewList.add(ConstantTable.ORG_LEVEL_VIEW_PREFIX + level);
            }

            queryParam.put("views",levelViewList);
            return bmSysOrganizationDao.queryListInLevel(queryParam);
        }*/
    }

    @Override
    public int queryListInLevelTotal(Query queryParam) {
        //获取机构层数
        String valueStr = sysConfigService.getValue(ConstantTable.CONFIG_ORGLEVELCOUNT);
        int levelCount = Integer.parseInt(valueStr);

        if (0 == levelCount){
            //没有下属子孙机构
            return 0;
        } else {


            //所有子孙机构视图表名
            List<String> levelViewList = new ArrayList<>(levelCount);

            String level = (String) queryParam.get("level");
            if (StringUtils.isBlank(level) || "0".equals(level)) {
                //获取所有子孙机构

                //构建所有子孙机构视图表名
                for (int i = levelCount; i > 0; i--) {
                    levelViewList.add(ConstantTable.ORG_LEVEL_VIEW_PREFIX + i);
                }

            } else {
                //获取指定层别机构

                //构建指定层别机构视图名
                levelViewList.add(ConstantTable.ORG_LEVEL_VIEW_PREFIX + level);
            }
            queryParam.put("views",levelViewList);
        }

        return bmSysOrganizationDao.queryListInLevelTotal(queryParam);
    }

    @Override
    public void setUserCountWithOrg(List<BmSysOrganizationWithParent> bmSysOrganizations) {
        if (null != bmSysOrganizations && bmSysOrganizations.size() > 0){
            List<String> orgids = new ArrayList<>(bmSysOrganizations.size());
            for (BmSysOrganizationWithParent org: bmSysOrganizations
                 ) {
                orgids.add(org.getOrgId());
            }
            List<Map<String, Object>> userCountWithOrg = getUserCountWithOrg(orgids);
            for (Map<String, Object> userCount:userCountWithOrg
                 ) {
                String orgid = String.valueOf(userCount.get("org_id"));
                String count = String.valueOf(userCount.get("count"));

                for (BmSysOrganizationWithParent org: bmSysOrganizations
                        ) {
                   if (orgid .equals(org.getOrgId())){
                       org.setUserCount(Integer.parseInt(count));
                       break;
                   }
                }
            }
        }
    }

    @Override
    public List<Map<String, Object>> getUserCountWithOrg(List<String> ids) {
        return bmSysOrganizationDao.getUserCountWithOrg(ids);
    }

    @Override
    public List<BmSysOrganizationWithParent> queryListInLevelWith(Query queryParam) {
        createCondition(queryParam);
        Integer levelCount = (Integer) queryParam.get("levelCount");

        if (0 == levelCount){
            //没有下属子孙机构
            return new ArrayList<BmSysOrganizationWithParent>();
        }else {
            queryParam.put("withParent","withParent");//获取父级机构信息
            return bmSysOrganizationDao.queryListInLevelWith(queryParam);
        }
    }

    /**
     * 构造查询条件
     * @param queryParam
     */
    private void createCondition(Query queryParam){
        //获取机构层数
        String valueStr = sysConfigService.getValue(ConstantTable.CONFIG_ORGLEVELCOUNT);
        int levelCount = Integer.parseInt(valueStr);

        if (0 == levelCount){
            //没有下属子孙机构
            queryParam.put("levelCount",0);
        } else {
            queryParam.put("levelCount",1);

            //所有子孙机构视图表名
            List<String > levelViewList = new ArrayList<>(levelCount);

            String level = (String)queryParam.get("level");
            if (StringUtils.isBlank(level) || "0" .equals(level)){
                //获取所有子孙机构

                //构建所有子孙机构视图表名
                for (int i = levelCount; i >0; i --){
                    levelViewList.add(ConstantTable.ORG_LEVEL_VIEW_PREFIX + i);
                }

            } else {
                //获取指定层别机构

                //构建指定层别机构视图名
                levelViewList.add(ConstantTable.ORG_LEVEL_VIEW_PREFIX + level);
            }

            queryParam.put("views",levelViewList);
        }
    }

    @Override
    public BmSysOrganization queryByOrgID(BmSysOrganization temp) {
        List<BmSysOrganization> bmSysOrganizations = bmSysOrganizationDao.queryListByOrg(temp);
        if (null != bmSysOrganizations && bmSysOrganizations.size() > 0){
            return bmSysOrganizations.get(0);
        }
        return null;
    }
}
