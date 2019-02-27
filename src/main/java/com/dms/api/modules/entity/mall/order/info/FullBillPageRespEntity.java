package com.dms.api.modules.entity.mall.order.info;

import lombok.Data;

import java.util.List;

/**
 * @Author: hejiaxi
 * @CreateDate: 2018/9/10 12:01
 * @Description: 作用描述
 */
@Data
public class FullBillPageRespEntity {

    private String pageNo;      //当前页
    private String totalPage;   //总页数
    private String count;       //总条数
    private String pageSize;    //每页大小
    private List<FullBillListEntity> list;
}
