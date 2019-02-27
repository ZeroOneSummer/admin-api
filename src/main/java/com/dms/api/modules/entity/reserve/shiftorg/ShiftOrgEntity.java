package com.dms.api.modules.entity.reserve.shiftorg;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @author 40
 * date 2018/1/9
 * time 10:50
 * decription:转移机构实体类
 */
@Data
public class ShiftOrgEntity {

    private Long id;

    @Excel(name = "用户名", orderNum = "1", isImportField = "userName", width = 20)
    private String userName;

    @Excel(name = "登录账号", orderNum = "2", isImportField = "loginCode", width = 50)
    private String loginCode;

    @Excel(name = "转移机构推荐码", orderNum = "3", isImportField = "serialCode", width = 20)
    private String serialCode;

}
