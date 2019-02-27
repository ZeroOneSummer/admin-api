package com.dms.api.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisService implements MessageListener {
    @Autowired
    @Qualifier("noSerializerRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private ValueOperations<String, String> valueOperations;
    @Autowired
    @Qualifier("listOperationsSerial")
    private ListOperations<String, Object> listOperationsSerial;
    @Autowired
    private SetOperations<String, Object> setOperations;

    /**
     * 默认过期时长，单位：秒
     */
    public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
    /**
     * 不设置过期时长
     */
    public final static long NOT_EXPIRE = -1;
    private final static Gson gson = new Gson();

//    public void set(String key, Object value, long expire){
//        valueOperations.set(key, toJson(value));
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
//    }

//    public void set(String key, Object value){
//        set(key, value, DEFAULT_EXPIRE);
//    }
//
//    public <T> T get(String key, Class<T> clazz, long expire) {
//        String value = valueOperations.get(key);
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
//        return value == null ? null : fromJson(value, clazz);
//    }
//
//    public <T> T get(String key, Class<T> clazz) {
//        return get(key, clazz, NOT_EXPIRE);
//    }
//
//    public String get(String key, long expire) {
//        String value = valueOperations.get(key);
//        if(expire != NOT_EXPIRE){
//            redisTemplate.expire(key, expire, TimeUnit.SECONDS);
//        }
//        return value;
//    }
//
//    public String get(String key) {
//        return get(key, NOT_EXPIRE);
//    }
//
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Object转成JSON数据
     */
    private String toJson(Object object) {
        if (object instanceof Integer || object instanceof Long || object instanceof Float ||
                object instanceof Double || object instanceof Boolean || object instanceof String) {
            return String.valueOf(object);
        }

        return JSONObject.toJSONString(object);
    }

    /**
     * JSON数据，转成Object
     */
    private <T> T fromJson(String json, Class<T> clazz) {
        JSONObject objJson = JSONObject.parseObject(json);
        return JSONObject.toJavaObject(objJson, clazz);
    }

    /**
     * 模糊查询keys
     *
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }
//set    

    /**
     * 添加set
     *
     * @param key
     * @param value
     */
    public void sadd(String key, String... value) {
        redisTemplate.boundSetOps(key).add(value);
    }

    /**
     * 删除set集合中的对象
     *
     * @param key
     * @param value
     */
    public void srem(String key, String... value) {
        redisTemplate.boundSetOps(key).remove(value);
    }

    public Set<Object> smember(String key) {
        Set<Object> members = setOperations.members(key);
        return members;
    }

    //map

    /**
     * 将map写入缓存
     *
     * @param key
     * @param map
     * @param time 失效时间(秒)
     */
    public <T> void setMap(String key, Map<String, T> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }



    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key
     * @param map
     */
    public <T> void addMap(String key, Map<String, T> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key   cache对象key
     * @param field map对应的key
     * @param value 值
     */
    public void addMap(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key   cache对象key
     * @param field map对应的key
     * @param obj   对象
     */
    public <T> void addMap(String key, String field, T obj) {
        redisTemplate.opsForHash().put(key, field, obj);
    }

    /**
     * 获取map缓存
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> Map<String, T> mget(String key, Class<T> clazz) {
        BoundHashOperations<String, String, T> boundHashOperations = redisTemplate.boundHashOps(key);
        return boundHashOperations.entries();
    }

    /**
     * 获取map缓存
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> T getMap(String key, Class<T> clazz) {
        BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
        Map<String, String> map = boundHashOperations.entries();
        String objStr = map.get(key);
        JSONObject json = JSONObject.parseObject(objStr);
        return JSONObject.toJavaObject(json, clazz);
    }

    public LinkedHashMap<String, Object> getMap(String key) {
        BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(key);
        LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) boundHashOperations.entries();
        LinkedHashMap<String, Object> rMap = new LinkedHashMap<>();
        Iterator<String> it = map.keySet().iterator();
        while (it.hasNext()) {
            String n = it.next();
            rMap.put(n, map.get(n));
        }
        return rMap;
    }

    /**
     * 获取map缓存中的某个对象
     *
     * @param key
     * @param field
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getMapField(String key, String field, Class<T> clazz) {
        return (T) redisTemplate.boundHashOps(key).get(field);
    }

    /**
     * 删除map中的某个对象
     *
     * @param key   map对应的key
     * @param field map中该对象的key
     * @author lh
     * @date 2016年8月10日
     */
    public void delMapField(String key, String... field) {
        BoundHashOperations<String, String, ?> boundHashOperations = redisTemplate.boundHashOps(key);
        boundHashOperations.delete(field);
    }

    //value

    /**
     * 将value对象以JSON格式写入缓存
     *
     * @param key
     * @param value
     * @param time  失效时间(秒)
     */
    public void setJson(String key, Object value, long time) {
        valueOperations.set(key, JSONObject.toJSONString(value));
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }


    /**
     * 递减操作
     *
     * @param key
     * @param by
     * @return
     */
    public double decr(String key, double by) {
        return redisTemplate.opsForValue().increment(key, -by);
    }

    /**
     * 递增操作
     *
     * @param key
     * @param by
     * @return
     */
    public double incr(String key, double by) {
        return redisTemplate.opsForValue().increment(key, by);
    }

    /**
     * 获取double类型值
     *
     * @param key
     * @return
     */
    public double getDouble(String key) {
        String value = (String) redisTemplate.boundValueOps(key).get();
        if (StringUtils.isNotBlank(value)) {
            return Double.valueOf(value);
        }
        return 0d;
    }

    /**
     * 设置double类型值
     *
     * @param key
     * @param value
     * @param time  失效时间(秒)
     */
    public void setDouble(String key, double value, long time) {
        valueOperations.set(key, String.valueOf(value));
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 设置double类型值
     *
     * @param key
     * @param value
     * @param time  失效时间(秒)
     */
    public void setInt(String key, int value, long time) {
        valueOperations.set(key, String.valueOf(value));
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 删除缓存<br>
     * 根据key精确匹配删除
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    public void del(Object[] keys){
        if (keys != null && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0].toString());
            } else {
                redisTemplate.delete(Arrays.asList(keys).toString());
            }
        }
    }

    /**
     * 批量删除<br>
     * （该操作会执行模糊查询，请尽量不要使用，以免影响性能或误删）
     *
     * @param pattern
     */
    public void batchDel(String... pattern) {
        for (String kp : pattern) {
            redisTemplate.delete(redisTemplate.keys(kp + "*"));
        }
    }

    /**
     * 取得缓存（int型）
     *
     * @param key
     * @return
     */
    public Integer getInt(String key) {
        String value = (String) redisTemplate.boundValueOps(key).get();
        if (StringUtils.isNotBlank(value)) {
            return Integer.valueOf(value);
        }
        return null;
    }

    /**
     * 取得缓存（字符串类型）
     *
     * @param key
     * @return
     */
    public String getStr(String key) {
        return (String) redisTemplate.boundValueOps(key).get();
    }

    /**
     * 取得缓存（字符串类型）
     *
     * @param key
     * @return
     */
    public String getStr(String key, boolean retain) {
        String value = (String) redisTemplate.boundValueOps(key).get();
        if (!retain) {
            redisTemplate.delete(key);
        }
        return value;
    }

    /**
     * 获取缓存<br>
     * 注：基本数据类型(Character除外)，请直接使用get(String key, Class<T> clazz)取值
     *
     * @param key
     * @return
     */
    public Object getObj(String key) {
        return redisTemplate.boundValueOps(key).get();
    }

    /**
     * 获取缓存<br>
     * 注：java 8种基本类型的数据请直接使用get(String key, Class<T> clazz)取值
     *
     * @param key
     * @param retain 是否保留
     * @return
     */
    public Object getObj(String key, boolean retain) {
        Object obj = redisTemplate.boundValueOps(key).get();
        if (!retain) {
            redisTemplate.delete(key);
        }
        return obj;
    }

    /**
     * 获取缓存<br>
     * 注：该方法暂不支持Character数据类型
     *
     * @param key   key
     * @param clazz 类型
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        return (T) redisTemplate.boundValueOps(key).get();
    }

//    /** 
//     * 获取缓存json对象<br> 
//     * @param key   key 
//     * @param clazz 类型 
//     * @return 
//     */  
//    public  <T> T getJson(String key, Class<T> clazz) {  
//        return JSONObject.fromJsonString(redisTemplate.boundValueOps(key).get(), clazz);  
//    }  

    /**
     * 将value对象写入缓存
     *
     * @param key
     * @param value
     * @param time  失效时间(秒)
     */
    public void set(String key, Object value, long time) {
        if (value.getClass().equals(String.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Integer.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Double.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Float.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Short.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Long.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Boolean.class)) {
            valueOperations.set(key, value.toString());
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 将value对象写入缓存 ，无失效时间。
     *
     * @param key
     * @param value
     */
    public void noTimeset(String key, Object value) {
        if (value.getClass().equals(String.class)) {

        } else if (value.getClass().equals(Integer.class)) {  valueOperations.set(key, value.toString());
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Double.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Float.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Short.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Long.class)) {
            valueOperations.set(key, value.toString());
        } else if (value.getClass().equals(Boolean.class)) {
            valueOperations.set(key, value.toString());
        } else {
            redisTemplate.opsForValue().set(key, value);
        }
    }


    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody();//请使用valueSerializer
        byte[] channel = message.getChannel();
        //请参考配置文件，本例中key，value的序列化方式均为string。
        //其中key必须为stringSerializer。和redisTemplate.convertAndSend对应
        LinkedHashMap<String, Object> itemValue = (LinkedHashMap<String, Object>) redisTemplate.getValueSerializer().deserialize(body);
        String topic = redisTemplate.getStringSerializer().deserialize(channel);
        List<WebSocketSession> webSocketSessions = new ArrayList<>();

        webSocketSessions.forEach(webSocketSession -> {
            HashMap<String, Object> resMap = (HashMap<String, Object>) webSocketSession.getAttributes().get("message");
            List<String> time = (List<String>) resMap.get("time");
            List<String> data = (List<String>) resMap.get("data");
            Long sTime = Long.parseLong(time.get(0));
            Long bTime = Long.parseLong(itemValue.get("time") + "");
            time.clear();
            data.clear();
            time.add(sTime > bTime ? sTime + "" : bTime + "");
            data.add(itemValue.get("total") + "");
            resMap.put("time", time);
            resMap.put("data", data);
            webSocketSession.getAttributes().put("message", resMap);
        });
    }
}
