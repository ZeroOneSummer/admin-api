package com.dms.api.modules.service.report.approvalquery;


import com.dms.api.common.utils.R;

import java.util.Map;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/7 11:40
 * @Description: 作用描述
 */
public interface AuditHistoryService {

    R queryList(Map<String, String> Params);
}
