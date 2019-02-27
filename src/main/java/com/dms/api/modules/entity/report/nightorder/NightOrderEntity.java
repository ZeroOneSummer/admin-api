package com.dms.api.modules.entity.report.nightorder;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 过夜单统计
 */
@Data
public class NightOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Excel(name = "年周数", isImportField = "weeks", width = 25)
    private String weeks;   //年周数

    @Excel(name = "服务商编号", isImportField = "serviceNum", width = 25)
    private String serviceNum;   //服务商编号

    @Excel(name = "数值", isImportField = "dim", width = 25, numFormat = "0.000")
    private BigDecimal dim; //数值（暂定）

    @Excel(name = "开始日期", isImportField = "beginDate", width = 50)
    private String beginDate; //开始日期

    @Excel(name = "结束日期", isImportField = "endDate", width= 50)
    private String endDate; //结束日期

}