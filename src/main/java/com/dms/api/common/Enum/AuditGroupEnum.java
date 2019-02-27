package com.dms.api.common.Enum;

/**
 * class_name: 审批组类型枚举
 * @author Ager
 **/
public enum AuditGroupEnum {

    CUSTOMER_SERVICES(1, "customer_Services"),//售后客服专员
    CUSTOMER_SERVICES_CHIEF_INSPECTOR(2, "customer_Services_chief_inspector"),//客服总监
    DEPCL(3, "depcl"),//渠道部员工
    DEPCL_MANAGEA(4, "depcl_managea"),//渠道部经理
    EMPGROUP(5, "empgroup"),//员工组
    FACILITATOR(6, "facilitator"),//服务商
    FINANCE_DEP_MANAGER(7, "finance_dep_manager"),//财务部总监
    MANAGEGROUP(8, "managegroup"),//经理组
    SPOT_DEP_STAFF(9, "spot_dep_staff"),//现货部员工
    TELLER(10, "teller");//出纳专员

    private Integer value;
    private String desc;

    AuditGroupEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
