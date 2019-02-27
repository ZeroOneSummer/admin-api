package com.dms.api.modules.dao.reserve.shiftorg;

import com.dms.api.modules.base.BaseDao;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgRecordEntity;
import com.dms.api.modules.entity.reserve.shiftorg.ShiftOrgVerifyEntity;
import com.dms.api.modules.entity.sys.config.SysConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户转移机构记录表
 * @author 40
 * @email chenshiling@danpacmall.com
 * @date 2018-01-09 13:53:46
 */
@Mapper
public interface ShiftOrgRecordDao extends BaseDao<ShiftOrgRecordEntity> {

	int insertBatch(List <ShiftOrgVerifyEntity> list);

	int updateBatch(List <ShiftOrgRecordEntity> list);

	int verifyBatch(Map <String, Object> map);

	List<ShiftOrgRecordEntity> checkRepeat(List <ShiftOrgRecordEntity> list);

	/**
	 * 查询待转移列表
	 */
	List<ShiftOrgRecordEntity> queryShiftList();

	List<ShiftOrgVerifyEntity> queryVerifyList(Map <String, Object> map);

	int queryVerifyTotal(Map <String, Object> map);

	/*立即转机构*/
	List<Map<String,Object>> orderCount(@Param("logincodes") List <String> logincodes, @Param("datestr") Map <String, Object> dateStr);

	/*定时转机构*/
	List<Map<String,Object>> orderCount2(@Param("logincodes") List <String> logincodes, @Param("datestr") Map <String, Object> dateStr);

	/**
	 * 获取用户的归属机构
	 * @param shiftOrgRecordEntities
	 * @return
	 */
	List<Map<String,Object>> getUserOrg(List <ShiftOrgVerifyEntity> shiftOrgRecordEntities);

	/**
	 * 获取目标机构推荐码
	 * @param shiftOrgRecordEntities
	 * @return
	 */
	List<Map<String,Object>> getTargetOrgSerialCode(List <ShiftOrgVerifyEntity> shiftOrgRecordEntities);

	/**
	 * 获取默认机构list
	 * @param
	 * @return
	 */
	SysConfigEntity getDefaultOrg();

	int updateVerifyRecordStatus(@Param("vId") Long vId, @Param("remark") String remark);
}
