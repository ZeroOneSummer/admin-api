package com.dms.api.modules.controller.mall.order.refund;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.dms.api.common.annotation.SysLog;
import com.dms.api.common.utils.*;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyAndVerifyRecordVo;
import com.dms.api.modules.entity.mall.order.refund.RefundApplyEntity;
import com.dms.api.modules.service.mall.order.refund.RefundApplyService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 退差价申请表
 */
@RestController
@RequestMapping("refundapply")
public class RefundApplyController extends AbstractController {

    @Autowired
    private RefundApplyService refundApplyService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("refundapply:list")
    public R list(@RequestParam Map<String, Object> params , HttpSession session) {
        //查询列表数据
        Query query = new Query(params);
        //驼峰转换，用于排序
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));
        //条件存储在session里，以便导报表
        //session.setAttribute("query_excel", query);
        List<RefundApplyAndVerifyRecordVo> refundApplyAndVerifyRecordVoList = refundApplyService.refundApplyAndVerifyRecordVoList(query);
        int total = refundApplyService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(refundApplyAndVerifyRecordVoList, total, query);
        return R.ok().put("page", pageUtil);
    }

    /**
     * 
    * @author jp   
    * @Description: 退差价报表  
    * @date 2018年4月11日 下午1:50:54  
    * @version V1.0
    * @return void
     */
    @RequestMapping("/download")
    @RequiresPermissions("refundapply:download")
    public void download(@RequestParam Map<String, Object> params,HttpServletRequest request, HttpServletResponse response) throws Exception {
        //查询列表数据
        //获取当前查询条件
		//Query query = (Query) request.getSession().getAttribute("query_excel");
		Query query = new Query(params);
		//是否分页（downLoad为true时省略分页）
        query.put("downLoad",true);
        //创建容器
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));
        List<RefundApplyAndVerifyRecordVo> list = refundApplyService.refundApplyAndVerifyRecordVoList(query); 

        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(),RefundApplyAndVerifyRecordVo.class,list);

        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 下载文件的默认名称
        String dataStr = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        response.setHeader("Content-Disposition", "attachment;filename="+new String((dataStr+"退差价记录表").getBytes("utf-8"), "iso8859-1")+".xls");
        workbook.write(response.getOutputStream());
    }

    /**
     * @param id：订单orderCode,
     * @param type:类型,0位初审，1复审 信息
     */
    @RequestMapping("/info/{id}/{type}")
    @RequiresPermissions("refundapply:info")
    public R info(@PathVariable("id") Long id, @PathVariable("type") long type) {
        RefundApplyAndVerifyRecordVo refundApplyAndVerifyRecordVo = refundApplyService.info(id, type);
        return R.ok().put("refundApplyAndVerifyRecordVo", refundApplyAndVerifyRecordVo);
    }

    /**
     * 提交审核
     */
    @RequestMapping("/commitApplyFirst")
    @RequiresPermissions("refundapply:commitApply:first")
    @SysLog("提交初审")
    public R commitApplyFirst(@RequestBody RefundApplyAndVerifyRecordVo refundApplyAndVerifyRecordVo) {
        if (refundApplyAndVerifyRecordVo.getType() == 0 && (refundApplyAndVerifyRecordVo.getStatus() == 1 || refundApplyAndVerifyRecordVo.getStatus() == -1)) {
            return refundApplyService.commitApply(refundApplyAndVerifyRecordVo, getUserId());
        } else {
            return R.error("提交审查出错！");
        }
    }

    /**
     * 提交审核
     */
    @RequestMapping("/commitApplySecond")
    @RequiresPermissions("refundapply:commitApply:second")
    @SysLog("提交二审")
    public R commitApplySecond(@RequestBody RefundApplyAndVerifyRecordVo refundApplyAndVerifyRecordVo) {
        if (refundApplyAndVerifyRecordVo.getType() == 1 && refundApplyAndVerifyRecordVo.getStatus() == 1) {
            refundApplyAndVerifyRecordVo.setStatus(2);
        } else if (refundApplyAndVerifyRecordVo.getType() != 1 && refundApplyAndVerifyRecordVo.getStatus() != -1) {
            return R.error("提交审查出错！");
        }
        return refundApplyService.commitApply(refundApplyAndVerifyRecordVo, getUserId());
    }

    /**
     * 提交审核
     */
    @RequestMapping("/commitApplyThird")
    @RequiresPermissions("refundapply:commitApply:third")
    @SysLog("提交三审")
    public R commitApplyThird(@RequestBody RefundApplyAndVerifyRecordVo refundApplyAndVerifyRecordVo) {
        if (refundApplyAndVerifyRecordVo.getType() == 2 && refundApplyAndVerifyRecordVo.getStatus() == 1) {
            refundApplyAndVerifyRecordVo.setStatus(99);
        } else if (refundApplyAndVerifyRecordVo.getType() != 2 && refundApplyAndVerifyRecordVo.getStatus() != -1) {
            return R.error("提交审查出错！");
        }
        return refundApplyService.commitApply(refundApplyAndVerifyRecordVo, getUserId());
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
//	@RequiresPermissions("refundapply:save")
    public R save(@RequestBody RefundApplyEntity refundApply) {
        refundApplyService.save(refundApply);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
//	@RequiresPermissions("refundapply:update")
    public R update(@RequestBody RefundApplyEntity refundApply) {
        refundApplyService.update(refundApply);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
//	@RequiresPermissions("refundapply:delete")
    public R delete(@RequestBody Long[] ids) {
        refundApplyService.deleteBatch(ids);

        return R.ok();
    }

}
