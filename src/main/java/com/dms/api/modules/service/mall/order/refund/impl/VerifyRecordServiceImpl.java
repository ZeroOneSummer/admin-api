package com.dms.api.modules.service.mall.order.refund.impl;

import com.dms.api.modules.dao.mall.order.refund.VerifyRecordDao;
import com.dms.api.modules.entity.mall.order.refund.VerifyRecordEntity;
import com.dms.api.modules.service.mall.order.refund.VerifyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("verifyRecordService")
public class VerifyRecordServiceImpl implements VerifyRecordService {

	@Autowired
	private VerifyRecordDao verifyRecordDao;
	
	@Override
	public VerifyRecordEntity queryObject(Long id){
		return verifyRecordDao.queryObject(id);
	}
	
	@Override
	public List<VerifyRecordEntity> queryList(Map<String, Object> map){
		return verifyRecordDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return verifyRecordDao.queryTotal(map);
	}
	
	@Override
	public void save(VerifyRecordEntity verifyRecord){
		verifyRecordDao.save(verifyRecord);
	}
	
	@Override
	public void update(VerifyRecordEntity verifyRecord){
		verifyRecordDao.update(verifyRecord);
	}
	
	@Override
	public void delete(Long id){
		verifyRecordDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		verifyRecordDao.deleteBatch(ids);
	}
	
}
