package com.dms.api.modules.entity.reserve.userinfo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * @program: S-sys-admin-api
 * @Date: 2018/12/4 0004 16:25
 * @Author: hushuangxi
 * @Description:
 */
@Data
public class UserParamChangeRecordEntity {

    //编号id	   客户姓名username	登录账号logincode	止盈比例paramTp	止损比例paramCl
    //偏离点位paramTs	免密金额amounts	提交时间createTime	修改时间updateTime
    @Excel(name = "编号", mergeVertical = false, isImportField = "id", width = 10)
    private String id;

    @Excel(name = "客户姓名", mergeVertical = false, isImportField = "username", width = 25)
    private String username;

    @Excel(name = "登录账号", mergeVertical = false, isImportField = "logincode", width = 50)
    private String logincode;

    @Excel(name = "联系电话", mergeVertical = false, isImportField = "phone", width = 25)
    private String phone;

    @Excel(name = "止盈比例", mergeVertical = false, isImportField = "paramTp", width = 25)
    private String paramTp;

    @Excel(name = "止损比例", mergeVertical = false, isImportField = "paramCl", width = 25)
    private String paramCl;

    @Excel(name = "偏离点位", mergeVertical = false, isImportField = "paramTs", width = 25)
    private String paramTs;

    @Excel(name = "免密金额", mergeVertical = false, isImportField = "amounts", width = 25)
    private String amounts;

    @Excel(name = "提交时间", mergeVertical = false, isImportField = "createTime", width = 25)
    private String createTime;

    @Excel(name = "修改时间", mergeVertical = false, isImportField = "updateTime", width = 25)
    private String updateTime;

}
