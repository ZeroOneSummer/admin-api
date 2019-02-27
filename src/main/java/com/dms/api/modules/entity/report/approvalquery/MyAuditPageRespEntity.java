package com.dms.api.modules.entity.report.approvalquery;

import java.util.List;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/11 11:18
 * @Description: 作用描述
 */
public class MyAuditPageRespEntity {
    private String total;
    private List<MyAuditEntity> list;

    public String getTotal() {
        return total;
    }

    public MyAuditPageRespEntity setTotal(String total) {
        this.total = total;
        return this;
    }

    public List<MyAuditEntity> getList() {
        return list;
    }

    public MyAuditPageRespEntity setList(List<MyAuditEntity> list) {
        this.list = list;
        return this;
    }
}
