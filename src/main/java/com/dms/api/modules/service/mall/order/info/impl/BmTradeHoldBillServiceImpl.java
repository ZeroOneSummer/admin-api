package com.dms.api.modules.service.mall.order.info.impl;

import com.dms.api.common.Enum.ChartType;
import com.dms.api.common.utils.*;
import com.dms.api.modules.dao.basis.org.BmDealerInfoDao;
import com.dms.api.modules.dao.mall.order.info.BmSymbolInfoDao;
import com.dms.api.modules.dao.mall.order.info.BmTradeHoldBillDao;
import com.dms.api.modules.dao.mall.order.info.BmTradeOrderDao;
import com.dms.api.modules.dao.mall.order.info.BmUserAccountDao;
import com.dms.api.modules.entity.basis.org.BmDealerInfo;
import com.dms.api.modules.entity.mall.order.info.BmSymbolInfoEntity;
import com.dms.api.modules.entity.mall.order.info.BmTradeHoldBill;
import com.dms.api.modules.entity.mall.order.info.BmTradeOrder;
import com.dms.api.modules.entity.mall.order.info.BmUserAccountEntity;
import com.dms.api.modules.service.mall.order.info.BmTradeHoldBillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * BmTradeHoldBillServiceImpl
 */
@Service
public class BmTradeHoldBillServiceImpl implements BmTradeHoldBillService {

    Logger logger = LoggerFactory.getLogger(BmTradeHoldBillServiceImpl.class);

    @Autowired
    BmTradeHoldBillDao bmTradeHoldBillDao;

    @Autowired
    BmDealerInfoDao bmDealerInfoDao;

    @Autowired
    private BmSymbolInfoDao bmSymbolInfoDao;

    @Autowired
    private BmUserAccountDao bmUserAccountDao;

    @Autowired
    BmTradeOrderDao bmTradeOrderDao;

    @Override
    public List<BmTradeHoldBill> queryList(Map<String, Object> map) {
        //获取订单列表
        List<BmTradeHoldBill> list = bmTradeHoldBillDao.querySimpleList(map);
        //获取订单列表对应信息
        map.put("list",list);
        List<BmSymbolInfoEntity> symbols = bmSymbolInfoDao.queryListByIds(map);
        List<BmUserAccountEntity> users = bmUserAccountDao.queryListByAccId(map);
        list.forEach(record->{
            symbols.parallelStream()
                    .filter(entity -> (record.getSymbolId()+"").equals(entity.getSymbolId()))
                    .findFirst()
                    .ifPresent(entity->{
                        record.setSymbolName(entity.getSymbolName());
                    });
            users.parallelStream()
                    .filter(entity -> record.getAccId().equals(entity.getAccId()))
                    .findFirst()
                    .ifPresent(entity->{
                        record.setUserName(entity.getUserName());
                    });
        });
        return list;
    }

    @Override
    public Integer queryTotal(Map<String, Object> map) {
        return bmTradeHoldBillDao.queryTotal(map);
    }

    @Override
    public BmTradeHoldBill queryObject(Long id) {
        BmTradeHoldBill bmTradeHoldBill = bmTradeHoldBillDao.queryObject(id);
        if (null != bmTradeHoldBill) {
            String openDate = bmTradeHoldBill.getOpenDate();
            String closeDate = bmTradeHoldBill.getCloseDate();
            bmTradeHoldBill.setOpenDate(null != openDate ? formatTtime(openDate) : null);
            bmTradeHoldBill.setCloseDate(null != closeDate ? formatTtime(closeDate) : null);
        }
        return bmTradeHoldBill;
    }

    @Override
    public HashMap<String, Object> historyData(Map record) {
        ChartType chartType = (ChartType) record.get("chartType");
        record.put("symbolCode", chartType.getMaterial());
        record.put("bsCode", chartType.getBsCode());
        Long queryTime = (Long) record.get("date");
        Long startTime;
        Long endtTime;
        //没有传查询时间则查询当天时间
        LocalDateTime l = null == queryTime ? DateUtils.getCustomQueryTime(new Date().getTime()) : DateUtils.getCustomQueryTime(queryTime);
        startTime = Timestamp.valueOf(l).getTime();
        record.put("startTime", getQueryTimeSt(l));
        l = l.getDayOfYear() != LocalDateTime.now().getDayOfYear() ? l.withDayOfYear(l.getDayOfYear() + 1) : LocalDateTime.now();
        record.put("endTime", getQueryTimeSt(l));
        endtTime = Timestamp.valueOf(l).getTime();

        Map<String,Object> param = new HashMap<>();
        Date dt = new Date(queryTime);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        param.put("endTime", df.format(dt));
        param.put("startTime", df.format(dt));
        param.put("dealerId", record.get("dealerId"));

        String type = chartType.getType();

        //查询当天的历史单据信息
        List<BmTradeOrder> blist =  bmTradeOrderDao.selectByDate_chart(param);

        List<Map<String ,Object >> dataTemp = new ArrayList<>(); //时间点与数据对应 === lTime
        List<List<BigDecimal>> lDataArray = new ArrayList<>();
        List<Long> lTime = new ArrayList<>();

        try{

            //格式： 时间：{ b: { ag:100
            //                  au:90
            //                 },
            //              s: { ag:100
            //                  au:90
            //                 },
            //              sub: 100 预约回购差
            //            }
            Map<String,Object > timeData = new TreeMap<String, Object>();//时间点对应的数据综合

            String lastTimeKey = ""; //上一个时间点数据
            for (BmTradeOrder btemp : blist){

                //预约/回购/预约回购总差
                Map<String ,Object > weightMap = new HashMap<>(3);

                //时间点
                Long timeStamp = getTimeStamp(btemp.getOrderTime(), "yyyyMMdd'T'HHmmss");

                if ( ! timeData.containsKey(String.valueOf(timeStamp))){ //

                    if (! StringUtils.isBlank(lastTimeKey)){ //需要获取上一个时间点的数据进行累加

                        Map<String, Object> last = (Map<String, Object>) timeData.get(lastTimeKey);
                        weightMap.putAll(last);

                        timeData.put(String.valueOf(timeStamp),weightMap);

                    }else {
                        weightMap.put("sub",BigDecimal.ZERO);
                        timeData.put(String.valueOf(timeStamp),weightMap);
                    }

                    lastTimeKey = String.valueOf(timeStamp);

                }else {

                    weightMap = (Map<String,Object >)timeData.get(String.valueOf(timeStamp));
                }

                String bsCode = btemp.getBsCode(); //获取当前订单的bscode
                Map<String ,Object> bsCodeMap = null; //产品类型的数据：{au:100,ag:90}
                if ( ! weightMap.containsKey(bsCode)){

                    bsCodeMap = new HashMap<>();
                    bsCodeMap.put("AU",BigDecimal.ZERO);
                    bsCodeMap.put("AG",BigDecimal.ZERO);

                    weightMap.put(bsCode,bsCodeMap);


                }else{
                    Map<String, Object> stringObjectMap = (Map<String, Object>) weightMap.get(bsCode);
                    bsCodeMap = new HashMap<>();
                    bsCodeMap.putAll(stringObjectMap);

                    weightMap.put(bsCode,bsCodeMap);
                }


                String symbolId = btemp.getSymbolId();
                String symbolMark = "AU";
                if ("3".equals(symbolId)){
                    symbolMark = "AG";
                }


                if ( ! bsCodeMap.containsKey(symbolMark)){

                    if ("WEIGHT_TYPE".equals(type)){
                        Double contractSize = btemp.getContractSize();
                        Integer orderQuantity = btemp.getOrderQuantity();

                        BigDecimal count = new BigDecimal(String.valueOf(orderQuantity));
                        BigDecimal size = new BigDecimal(String.valueOf(contractSize));

                        bsCodeMap.put(symbolMark,count.multiply(size));

                    } else if ("PREPAY_TYPE".equals(type)){
                        BigDecimal marginUsed = btemp.getMarginUsed();
                        bsCodeMap.put(symbolMark,marginUsed);
                    }

                }else{


                    if ("WEIGHT_TYPE".equals(type)) {
                        BigDecimal o = (BigDecimal) bsCodeMap.get(symbolMark); //总克重

                        //size /数量
                        Double contractSize = btemp.getContractSize();
                        BigDecimal size = new BigDecimal(String.valueOf(contractSize));

                        Integer orderQuantity = btemp.getOrderQuantity();
                        BigDecimal count = new BigDecimal(String.valueOf(orderQuantity));

                        BigDecimal add = o.add((size.multiply(count)));

                        bsCodeMap.put(symbolMark, add);

                    } else if ("PREPAY_TYPE".equals(type)){
                        BigDecimal o = (BigDecimal) bsCodeMap.get(symbolMark);
                        BigDecimal marginUsed = btemp.getMarginUsed();

                        bsCodeMap.put(symbolMark,marginUsed.add(o));
                    }
                }

            }

            //计算各个时间点的预约回购差 并填充所有数据
            if ("WEIGHT_TYPE" .equals(type)){ //克重

                Set<Map.Entry<String, Object>> entries = timeData.entrySet();
                Iterator<Map.Entry<String, Object>> iterator = entries.iterator();

                while (iterator.hasNext()){
                    List<BigDecimal> datatemp = new ArrayList<>();

                    Map.Entry<String, Object> next = iterator.next();
                    Map<String,Object> value = (Map<String,Object>)next.getValue();

                    lTime.add(Long.parseLong(next.getKey()));//时间戳

                    BigDecimal auWeight_b = BigDecimal.ZERO;
                    BigDecimal agWeight_b = BigDecimal.ZERO;

                    BigDecimal auWeight_s = BigDecimal.ZERO;
                    BigDecimal agWeight_s = BigDecimal.ZERO;

                    //====bscode :b
                    Map<String,Object> b = (Map<String,Object>)value.get("b");
                    if (null != b){
                        Object auBscode_b = b.get("AU");
                        if( null != auBscode_b){
                            auWeight_b = (BigDecimal)auBscode_b;
                        }

                        Object agBscode_b = b.get("AG");
                        if( null != agBscode_b){
                            agWeight_b = (BigDecimal)agBscode_b;
                        }
                    }

                    //====bscode :s
                    Map<String,Object> s = (Map<String,Object>)value.get("s");
                    if (null != s){
                        Object auBscode_s = s.get("AU");
                        if( null != auBscode_s){
                            auWeight_s = (BigDecimal)auBscode_s;
                        }

                        Object agBscode_s = s.get("AG");
                        if( null != agBscode_s){
                            agWeight_s = (BigDecimal)agBscode_s;
                        }
                    }
                    //差额
                    BigDecimal addB = auWeight_b.add(agWeight_b);
                    BigDecimal addS = auWeight_s.add(agWeight_s);

                    value.put("sub",addB.subtract(addS));

                    datatemp.add(auWeight_b);
                    datatemp.add(agWeight_b);
                    datatemp.add(auWeight_s);
                    datatemp.add(agWeight_s);
                    datatemp.add(addB.subtract(addS).abs());

                    lDataArray.add(datatemp);
                }

            } else if ("PREPAY_TYPE".equals(type)){ //预付款

                Set<Map.Entry<String, Object>> entries = timeData.entrySet();
                Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
                while (iterator.hasNext()){
                    List<BigDecimal> datatemp = new ArrayList<>();
                    Map.Entry<String, Object> next = iterator.next();
                    Map<String,Object> value = (Map<String,Object>)next.getValue();

                    lTime.add(Long.parseLong(next.getKey()));//时间戳

                    BigDecimal auMoney_b = new BigDecimal("0");
                    BigDecimal agMoney_b = new BigDecimal("0");

                    BigDecimal auMoney_s = new BigDecimal("0");
                    BigDecimal agMoney_s = new BigDecimal("0");

                    //====bscode :b
                    Map<String,Object> b = (Map<String,Object>)value.get("b");
                    if (null != b){
                        Object auBscode_b = b.get("AU");
                        if( null != auBscode_b){
                            auMoney_b = (BigDecimal)auBscode_b;
                        }

                        Object agBscode_b = b.get("AG");
                        if( null != agBscode_b){
                            agMoney_b = (BigDecimal)agBscode_b;
                        }
                    }

                    //====bscode :s
                    Map<String,Object> s = (Map<String,Object>)value.get("s");
                    if (null != s){
                        Object auBscode_s = s.get("AU");
                        if( null != auBscode_s){
                            auMoney_s = (BigDecimal)auBscode_s;
                        }

                        Object agBscode_s = s.get("AG");
                        if( null != agBscode_s){
                            agMoney_s = (BigDecimal)agBscode_s;
                        }
                    }

                    datatemp.add(auMoney_b);
                    datatemp.add(agMoney_b);
                    datatemp.add(auMoney_s);
                    datatemp.add(agMoney_s);


                    datatemp.add((auMoney_b.add(agMoney_b)).subtract((auMoney_s.add(agMoney_s)).abs()));

                    datatemp.add(agMoney_b.add(agMoney_s));//预约回购金和
                    datatemp.add(auMoney_b.add(auMoney_s));//预约回购银和

                    lDataArray.add(datatemp);
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        Map<String, Object> resMap = loadData(type,lTime,lDataArray,startTime,endtTime) ; //loadData("",lTime, lDataArray, null, null);
        return (HashMap<String, Object>) resMap;
    }

    @Override
    public R historyDistribution(Map record) {
        List<HashMap<String, Object>> data = new ArrayList<>();
        Long queryTime = (Long) record.get("date");
        Long dealerId = null == record.get("dealerId") ? null : Long.parseLong(record.get("dealerId") + "");
        //没有传查询时间则查询当天时间
        LocalDateTime l = null == queryTime ? DateUtils.getCustomQueryTime(new Date().getTime()) : DateUtils.getCustomQueryTime(queryTime);
        record.put("startTime", getQueryTimeSt(l));
        l = l.getDayOfYear() != LocalDateTime.now().getDayOfYear() ? l.withDayOfYear(l.getDayOfYear() + 1) : LocalDateTime.now();
        record.put("endTime", getQueryTimeSt(l));
        //获得查询时间内订单
        List<BmTradeHoldBill> tradeHoldBills = bmTradeHoldBillDao.queryList(record);
        //获得商家列表
        List<BmDealerInfo> dealerInfos = bmDealerInfoDao.queryList(dealerId);
        Set<String> names = new HashSet<>();
        dealerInfos.forEach(bmDealerInfo -> {
            HashMap<String, Object> dealer = new HashMap<>();
            dealer.put("dealerName", bmDealerInfo.getDealerCode());
            Object[] dealersTrades = tradeHoldBills.parallelStream()
                    .filter(bmTradeHoldBill -> bmTradeHoldBill.getDealerCode().equals(bmDealerInfo.getDealerCode()))
                    .map(bmTradeHoldBill -> {
                        names.add(bmTradeHoldBill.getUserName());
                        List<String> dataInfo = new ArrayList<>();
                        dataInfo.add(DateUtils.getTTimeStamp(bmTradeHoldBill.getOpenDate()) + "");
                        dataInfo.add(bmTradeHoldBill.getUserName());
                        dataInfo.add(bmTradeHoldBill.getMarginUsed() + "");
                        dataInfo.add(Arith.mul(bmTradeHoldBill.getQuantityHold().floatValue(), bmTradeHoldBill.getContractSize()) + "");
                        dataInfo.add(bmTradeHoldBill.getSymbolName());
                        dataInfo.add("b".equals(bmTradeHoldBill.getBsCode()) ? "预订" : "回购");
                        dataInfo.add(bmTradeHoldBill.getQuantityHold() + "");
                        return dataInfo;
                    })
                    .toArray();
            if (dealersTrades.length != 0) {
                dealer.put("data", dealersTrades);
                data.add(dealer);


            }
        });


        Object sort = record.get("sort");//排序类型
            Map<String, BigDecimal> dataMap = new HashMap<>();
        if (null != sort && ! StringUtils.isBlank((String)sort) && data.size() > 0){
            String sortType = (String)sort;
            //排序条件
            List<HashMap<String, Object>> data2 = new ArrayList<>();

            for (HashMap<String, Object> temp:data){
                Object[] data1 = (Object[])temp.get("data");
                for (int i = data1.length -1 ;i >= 0; i--){
                    List<String> o = (List<String>)data1[i];
                    String name = o.get(1);

                    BigDecimal d = BigDecimal.ZERO;
                    if ("money".equals(sortType)) { //总付款
                        String s = o.get(3);
                        d = new BigDecimal(s);
                    }else if ("order_count".equals(sortType)){ //总克重
                        String s = o.get(6);
                        d = new BigDecimal(s);
                    }

                    if (dataMap.containsKey(name)){
                        BigDecimal bigDecimal = dataMap.get(name);
                        dataMap.put(name,bigDecimal.add(d));
                    }else {
                        dataMap.put(name,d);
                    }
                }
            }

            dataMap =  sortByComparator(dataMap);
            return R.ok().put("data", data).put("names", dataMap.keySet());
        }
            CommonUtils.listSort(data,"asc");
            return R.ok().put("data", data).put("names", names);

    }

    private String getQueryTimeSt(LocalDateTime l) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        return df.format(l);
    }

    private Map<String, Object> loadData(String chartype,List<Long> lTime, List<List<BigDecimal>> lData, Long nHourBefore, Long now) {
        Map<String, Object> dataMap = new HashMap<>();

        BigDecimal auWeight_b = new BigDecimal("0");
        BigDecimal agWeight_b = new BigDecimal("0");
        BigDecimal auWeight_s = new BigDecimal("0");
        BigDecimal agWeight_s = new BigDecimal("0");
        BigDecimal sub = new BigDecimal("0"); //预约回购差

        BigDecimal agMoney_s_b = new BigDecimal("0");
        BigDecimal auMoney_s_b = new BigDecimal("0");

        Long tmpTime = 0L;
        List<String> time = new ArrayList<>();

        List<List<BigDecimal>> resutlData = new ArrayList<>();
        List<BigDecimal>  tData = new ArrayList<>();

        for (int i = 0; i < lTime.size(); i++) {
            Long tTimeStamp = lTime.get(i);
            tData = lData.get(i);
            time.add(DateUtils.getTimeIndex(tTimeStamp));
            resutlData.add(tData);
            //time

        }
        if (time.size() <= 0) {
            time.add(DateUtils.getTimeIndex(nHourBefore));
            time.add(DateUtils.getTimeIndex(now));
            resutlData.add(tData);
            resutlData.add(tData);

        }


        List<String > result = new ArrayList<>();
        if (null != resutlData && resutlData.size() > 0){
            result = new ArrayList<>(resutlData.size());
            for (int i = 0; i <= resutlData.size() -1; i++ ){
                List<BigDecimal> bigDecimals = resutlData.get(i);
                if (null != bigDecimals && bigDecimals.size()  > 0){
                    result.add(ListToString(bigDecimals));
                }else {

                    if ("PREPAY_TYPE".equals(chartype)){
                        result.add("0,0,0,0,0,0,0");
//                        result.add("0,0,0,0,0,0,0");
                    }else {
                        result.add("0,0,0,0,0");
//                        result.add("0,0,0,0,0");
                    }
                }
            }
        }

        dataMap.put("time", time);
        dataMap.put("data", result);
        return dataMap;
    }

    private String formatTtime(String tTime) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");
        LocalDateTime l = LocalDateTime.parse(tTime, df);
        return df1.format(l);
    }

    public  Map<String,BigDecimal> sortByComparator(Map<String,BigDecimal> unsortMap){
        List list = new LinkedList(unsortMap.entrySet());
        Collections.sort(list, new Comparator()
        {
            @Override
            public int compare(Object o1, Object o2)
            {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });
        Map sortedMap = new LinkedHashMap();

        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;

    }

    private Long getTimeStamp(String tTime, String dfSt) {
        String tTime1 = tTime.split("T")[0];
        if (tTime1.length() <= 7) {
            tTime = tTime.substring(0, 6) + "0" + tTime.substring(6, 7) + "T" + tTime.split("T")[1];
        }
        return DateUtils.getTimeStamp(tTime, dfSt);
    }

    private String ListToString(List<?> list) {
        StringBuffer sb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) == null || list.get(i) == "") {
                    continue;
                }
                sb.append(list.get(i));
                sb.append(",");
            }
        }
        String str = sb.toString();
        return  sb.toString().substring(0,str.length()-1);
    }

}
