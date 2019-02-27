package com.dms.api.modules.service.reserve.shiftorg.impl;

import com.dms.api.common.utils.DateUtils;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.SpringContextUtils;
import com.dms.api.core.constants.ConstantTable;
import com.dms.api.modules.controller.reserve.shiftorg.UserShiftOrgTask;
import com.dms.api.modules.dao.mall.order.refund.VerifyRecordDao;
import com.dms.api.modules.dao.reserve.shiftorg.ShiftOrgRecordDao;
import com.dms.api.modules.entity.mall.order.refund.VerifyRecordEntity;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgRecordEntity;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgVerifyEntity;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import com.dms.api.modules.service.reserve.shiftorg.ShiftOrgRecordService;
import com.dms.api.modules.service.sys.config.SysConfigService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("shiftOrgRecordService")
public class ShiftOrgRecordServiceImpl implements ShiftOrgRecordService {

    @Autowired
    private ShiftOrgRecordDao shiftOrgRecordDao;

    @Autowired
    private VerifyRecordDao verifyRecordDao;

    @Override
    public ShiftOrgRecordEntity queryObject(Long id) {
        return shiftOrgRecordDao.queryObject(id);
    }

    @Override
    public List<ShiftOrgRecordEntity> queryList(Map<String, Object> map) {
        return shiftOrgRecordDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return shiftOrgRecordDao.queryTotal(map);
    }

    @Override
    public void save(ShiftOrgRecordEntity shiftOrgRecord) {
        shiftOrgRecordDao.save(shiftOrgRecord);
    }

    @Override
    public void update(ShiftOrgRecordEntity shiftOrgRecord) {
        shiftOrgRecordDao.update(shiftOrgRecord);
    }

    @Override
    public void delete(Long id) {
        shiftOrgRecordDao.delete(id);
    }

    @Override
    public void deleteBatch(Long[] ids) {
        shiftOrgRecordDao.deleteBatch(ids);
    }

    @Override
    public void reBack(Long id) {
        if(id!=null){
            shiftOrgRecordDao.updateVerifyRecordStatus(id,"待处理任务,手动驳回");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void verifyBatch(Map<String, Object> map) throws RuntimeException{
        map.put("referType",ConstantTable.SHIFT_ORG_VERIFY);
        int vNum = verifyRecordDao.verifyBatch(map);
        Date shiftTime = UserShiftOrgTask.tomorrow;//DateUtils.getTomorrow(6, 30, 0);


        //查库 机构转移参数配置 中得起始时间
        SysConfigService sysConfigService = SpringContextUtils.getBean(SysConfigService.class);
        String starttime = sysConfigService.getValue(ConstantTable.CONFIG_CACHE_KEY_SHIFT_ORG_START_TIME);
        if(StringUtils.isNotBlank(starttime)){
            String[] timefield = starttime.split(":");
            int hour = Integer.valueOf(timefield[0]);
            int minute = Integer.valueOf(timefield[1]);
            int second = Integer.valueOf(timefield[2]);
            UserShiftOrgTask.tomorrow=shiftTime = DateUtils.getTomorrow(hour, minute, second);//DateUtils.getTodayDate(hour, minute, second).getTime();
        }

        map.put("shiftTime",shiftTime);
        int sNum = shiftOrgRecordDao.verifyBatch(map);
        ArrayList ids = (ArrayList)map.get("ids");
        if (vNum != ids.size() || sNum != ids.size()){
            throw new RuntimeException("审核失败！");
        }
    }

    @Override
    public void insertBatch(List<ShiftOrgVerifyEntity> list) {
        shiftOrgRecordDao.insertBatch(list);
    }

    @Override
    public void updateBatch(List<ShiftOrgRecordEntity> list) {
        shiftOrgRecordDao.updateBatch(list);
    }

    @Override
    public List<ShiftOrgRecordEntity> checkRepeat(List<ShiftOrgRecordEntity> list) {
        return shiftOrgRecordDao.checkRepeat(list);
    }

    @Override
    public List<ShiftOrgRecordEntity> queryShiftList() {
        return shiftOrgRecordDao.queryShiftList();
    }

    @Override
    public List<ShiftOrgVerifyEntity> queryVerifyList(Map<String, Object> map) {
        return shiftOrgRecordDao.queryVerifyList(map);
    }
    @Override
    public int queryVerifyTotal(Map<String, Object> map) {
        return shiftOrgRecordDao.queryVerifyTotal(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R importFromExcel(List<ShiftOrgVerifyEntity> list) {

        //通过目标机构吗获取目标机构推荐码
        getTargetSerialCode(list);

        shiftOrgRecordDao.insertBatch(list);

        //检查源机构是否是客户归属机构
        checkUserOrg(list);

        //检查源机构是否属于可转机构（默认机构不可转）
        checkShiftOrg(list);

        //检查目标机构是否属于可转机构（默认机构不可转）
        checkTargetOrg(list);

        List<VerifyRecordEntity> vList = new ArrayList<>();
        list.forEach(entity -> {
            VerifyRecordEntity record = new VerifyRecordEntity();
            record.setType(entity.getType());
            record.setStatus(entity.getStatus());
            record.setReferType(ConstantTable.SHIFT_ORG_VERIFY);
            record.setReferId(entity.getReferId());
            record.setRemark(entity.getRemark());
            vList.add(record);
        });

        verifyRecordDao.insertBatch(vList);
        return R.ok();
    }



    @Override
    public List<Map<String,Object>> orderCount(List<String> logincodes, Map<String ,Object> dateStr) {
        return shiftOrgRecordDao.orderCount(logincodes,dateStr);
    }

    @Override
    public List<Map<String,Object>> orderCount2(List<String> logincodes, Map<String ,Object> dateStr) {
        return shiftOrgRecordDao.orderCount2(logincodes,dateStr);
    }

    private void checkUserOrg(List<ShiftOrgVerifyEntity> list){
        List<Map<String, Object>> userOrg = shiftOrgRecordDao.getUserOrg(list);
        for (ShiftOrgVerifyEntity shift:list) {
                boolean isOrg = false; //源机构是否是客户归属机构
                String loginCode = shift.getLoginCode();
                String srcServiceNumber = shift.getSrcOrgCode();
                shift.setType(0);
                for (Map<String, Object> userOrgInfo:userOrg) {
                    String logincodeTemp = (String)userOrgInfo.get("login_code");
                    String srServiceNumberTemp = (String)userOrgInfo.get("bm_org_num");
                    if (logincodeTemp .equals(loginCode) && srServiceNumberTemp .equals(srcServiceNumber)){ //源机构是客户归属机构
                        isOrg = true;
                        break;
                    }
                }

                if ( ! isOrg){
                    shift.setStatus(-1);
                    shift.setRemark("源机构信息不一致");
                }else {
                    shift.setStatus(0);
                    shift.setRemark("");
                }
        }
    }


    private void checkShiftOrg(List<ShiftOrgVerifyEntity> list){

        for (ShiftOrgVerifyEntity shift:list) {
            if(shift.getStatus() != null && shift.getStatus() == -1){
                continue;
            }
            boolean isOrg = true; //默认可以转机构
            String srcServiceNumber = shift.getSrcOrgCode(); //源机构编码
            //获取默认机构list
            SysConfigEntity sysConfig = shiftOrgRecordDao.getDefaultOrg();
            if (sysConfig != null){
                String[] defaultOrgs = sysConfig.getValue().split(",");
                if (defaultOrgs != null && defaultOrgs.length > 0){
                    for (String dfo:defaultOrgs) { //判断源机构是否属于默认机构
                        if (dfo.trim().equals(srcServiceNumber.trim())){
                            isOrg = false;
                            break;
                        }
                    }
                }
            }
            if ( ! isOrg){
                shift.setStatus(-1);
                shift.setRemark("源机构属于默认机构,无法进行转机构");
            }else {
                shift.setStatus(0);
                shift.setRemark("");
            }
        }
    }

    private void checkTargetOrg(List<ShiftOrgVerifyEntity> list){

        for (ShiftOrgVerifyEntity shift:list) {
            if(shift.getStatus() != null && shift.getStatus() == -1){
                continue;
            }
            boolean isOrg = true; //默认可以转机构
            String srcServiceNumber = shift.getTarOrgCode(); //目标机构编码
            //获取默认机构list
            SysConfigEntity sysConfig = shiftOrgRecordDao.getDefaultOrg();
            if (sysConfig != null){
                String[] defaultOrgs = sysConfig.getValue().split(",");
                if (defaultOrgs != null && defaultOrgs.length > 0){
                    for (String dfo:defaultOrgs) { //判断源机构是否属于默认机构
                        if (dfo.trim().equals(srcServiceNumber.trim())){
                            isOrg = false;
                            break;
                        }
                    }
                }
            }
            if ( ! isOrg){
                shift.setStatus(-1);
                shift.setRemark("目标机构属于默认机构,无法进行转机构");
            }else {
                shift.setStatus(0);
                shift.setRemark("");
            }
        }
    }

    /**
     * 通过目标机构码获取目标机构推荐码
     * @param list
     */
    private void getTargetSerialCode(List<ShiftOrgVerifyEntity> list) {
        List<Map<String, Object>> userOrg = shiftOrgRecordDao.getTargetOrgSerialCode(list);
        for (ShiftOrgVerifyEntity shift:list) {
            String targetServiceNumber = shift.getTarOrgCode();
            for (Map<String, Object> userOrgInfo:userOrg) {
                String targetServiceNumberTemp = (String)userOrgInfo.get("org_code");
                String targetSerialcode = (String)userOrgInfo.get("org_sequence");
                if (targetServiceNumber .equals(targetServiceNumberTemp)){ //源机构是客户归属机构
                    shift.setSerialCode(targetSerialcode);
                }
            }
        }
    }

}
