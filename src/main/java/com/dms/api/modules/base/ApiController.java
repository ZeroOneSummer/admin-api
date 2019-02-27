package com.dms.api.modules.base;

import com.alibaba.fastjson.JSONObject;
import com.dms.api.core.config.CertificateConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获取不同商城系统的关键词集合
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping("/getKeyWords")
	public JSONObject getKeyWordsByDiffMall(){
		logger.info("根据不同商城配置获取不同文案关键词");
		//响应实体
		JSONObject json = new JSONObject(){{put("beanName", CertificateConfig.beanName);}};
		logger.info("关键词：{}", json);

		return json;
	}

}
