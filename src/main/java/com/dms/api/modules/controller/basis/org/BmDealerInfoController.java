package com.dms.api.modules.controller.basis.org;

import com.dms.api.common.utils.PageUtils;
import com.dms.api.common.utils.Query;
import com.dms.api.common.utils.R;
import com.dms.api.common.utils.StringUtils;
import com.dms.api.modules.base.AbstractController;
import com.dms.api.modules.entity.basis.org.BmDealerInfo;
import com.dms.api.modules.entity.sys.user.SysUserEntity;
import com.dms.api.modules.service.basis.org.BmDealerInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 40
 * date 2017/10/25
 * time 15:19
 * decription:
 */
@RestController
@RequestMapping("/org/dealer")
public class BmDealerInfoController extends AbstractController {

    @Autowired
    BmDealerInfoService bmDealerInfoService;

    /**
     * 所有商家列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions("org:dealer:list")
    public R list(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        if (!getUser().isSuperAdmin()) {
            query.put("userId", getUserId());
        }
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));
        List<BmDealerInfo> dealerInfoList = bmDealerInfoService.queryList(query);
        int total = bmDealerInfoService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(dealerInfoList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 服务商列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/facilitatorList")
    @RequiresPermissions("org:dealer:facilitatorList")
    public R facilitatorList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        if (!getUser().isSuperAdmin()) {
            query.put("userId", getUserId());
        }
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));
        //服务商对应商家类型为3
        query.put("dealerType", 3);
        List<BmDealerInfo> dealerInfoList = bmDealerInfoService.queryList(query);
        int total = bmDealerInfoService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(dealerInfoList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }

    /**
     * 订货分部列表
     *
     * @param params
     * @return
     */
    @RequestMapping("/orderList")
    @RequiresPermissions("org:dealer:orderList")
    public R orderList(@RequestParam Map<String, Object> params) {
        //查询列表数据
        Query query = new Query(params);
        if (!getUser().isSuperAdmin()) {
            query.put("userId", getUserId());
        }
        query.put("sidx", StringUtils.camelToUnderline(query.get("sidx") + ""));
        //订货分部对应商家类型为1
        query.put("dealerType", 1);
        List<BmDealerInfo> dealerInfoList = bmDealerInfoService.queryList(query);
        int total = bmDealerInfoService.queryTotal(query);
        PageUtils pageUtil = new PageUtils(dealerInfoList, total, query.getLimit(), query.getPage());
        return R.ok().put("page", pageUtil);
    }


    @RequestMapping("/ids")
//    @RequiresPermissions("org:dealer:ids")
    public R idList() {
        SysUserEntity user = getUser();
        Long DealerId = user.isSuperAdmin() ? null : getUser().getDealerId();
        List<BmDealerInfo> dealers = bmDealerInfoService.getDealers(DealerId);
        return R.ok().put("dealers", dealers);
    }
    @RequestMapping("/orderIds")
//    @RequiresPermissions("org:dealer:ids")
    public R orderIds() {
        SysUserEntity user = getUser();
        Long DealerId = user.isSuperAdmin() ? null : getUser().getDealerId();
        Map<String,Object> params = new HashMap<>();
        params.put("dealerType",1);
        params.put("DealerId",DealerId);
        List<BmDealerInfo> dealers = bmDealerInfoService.getDealerInfos(params);
        return R.ok().put("dealers", dealers);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("org:dealer:info")
    public R info(@PathVariable("id") Long id) {
        BmDealerInfo bmDealerInfo = bmDealerInfoService.queryObject(id);

        return R.ok().put("bmOrganization", bmDealerInfo);
    }

}
