package com.dms.api.modules.entity.report.approvalquery;

import java.util.List;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/11 13:44
 * @Description: 作用描述
 */
public class AuditHistoryPageRespEntity {
    private String total;
    private List<AuditHistoryEntity> list;

    public String getTotal() {
        return total;
    }

    public AuditHistoryPageRespEntity setTotal(String total) {
        this.total = total;
        return this;
    }

    public List<AuditHistoryEntity> getList() {
        return list;
    }

    public AuditHistoryPageRespEntity setList(List<AuditHistoryEntity> list) {
        this.list = list;
        return this;
    }
}
