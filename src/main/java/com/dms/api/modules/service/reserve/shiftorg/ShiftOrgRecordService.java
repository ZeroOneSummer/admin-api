package com.dms.api.modules.service.reserve.shiftorg;

import com.dms.api.common.utils.R;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgRecordEntity;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgVerifyEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户转移机构记录表
 * @author 40
 * @email chenshiling@danpacmall.com
 * @date 2018-01-09 13:53:46
 */
public interface ShiftOrgRecordService {
	
	ShiftOrgRecordEntity queryObject(Long id);
	
	List<ShiftOrgRecordEntity> queryList(Map <String, Object> map);

	List<ShiftOrgVerifyEntity> queryVerifyList(Map <String, Object> map);

	int queryTotal(Map <String, Object> map);

	int queryVerifyTotal(Map <String, Object> map);

	void save(ShiftOrgRecordEntity shiftOrgRecord);

	void update(ShiftOrgRecordEntity shiftOrgRecord);

	void delete(Long id);

	void deleteBatch(Long[] ids);

	void reBack(Long id);
	/**
	 *批量审核
	 */
	void verifyBatch(Map <String, Object> map);

	void insertBatch(List <ShiftOrgVerifyEntity> list);

	void updateBatch(List <ShiftOrgRecordEntity> list);

	List<ShiftOrgRecordEntity> checkRepeat(List <ShiftOrgRecordEntity> list);

	List<ShiftOrgRecordEntity> queryShiftList();

	R importFromExcel(List <ShiftOrgVerifyEntity> list);

	/**
	 * 获取用户指定日期的订单数量情况
	 * @param logincodes 用户标志
	 * @param dateStr 订单时间，
	 * @return
	 */
	List<Map<String,Object>> orderCount(List <String> logincodes, Map <String, Object> dateStr);

	/*定时转机构*/
	List<Map<String,Object>> orderCount2(List <String> logincodes, Map <String, Object> dateStr);
}
