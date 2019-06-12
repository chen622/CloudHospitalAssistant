package cn.neuedu.his.service.impl;

import cn.neuedu.his.model.*;
import cn.neuedu.his.util.SerializeUtil;
import com.alibaba.fastjson.JSONObject;
import redis.clients.jedis.Jedis;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.jedis.exceptions.JedisException;

import java.net.ConnectException;
import java.util.*;

/**
 * default: DB0: constants
 * DB1: list
 * DB2: 暂存
 */
@Service
public class RedisServiceImpl {
    private static Logger logger = Logger.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;    //jedisPool不属于springboot框架支持的redis类,所以需要自行注入到spring中。通过配置类RedisConfig类注入的

    private String invoiceKey = "invoice-list";
    private String registrationKey = "-registration-list";
    private String medicalRecordKey = "MR";
    private String prescriptionKey = "prescriptions";
    private String applicationKey = "applications";

    //基础方法
    private Jedis getResource() {
        return jedisPool.getResource();
    }

    @SuppressWarnings("deprecation")
    private void returnResource(Jedis jedis) {
        if (jedis != null)
            jedisPool.returnResourceObject(jedis);
    }

    public void set(String key, String value) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    public String get(String key) throws Exception {
        String result;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    public String getSet(String key, String value) throws Exception {
        String result;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    public Long del(String key) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.del(key);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    private void setObject(String key, Object value) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    private Object getObject(String key) throws Exception {
        Object result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            byte[] data = jedis.get(key.getBytes());
            result = SerializeUtil.unserialize(data);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    private void setObjectList(String key, List<?> list) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(key.getBytes(), SerializeUtil.serializeList(list));
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    private List<?> getObjectList(String key) throws Exception {
        List<?> result = null;
        Jedis jedis = null;
        try {
            jedis = getResource();
            byte[] data = jedis.get(key.getBytes());
            result = SerializeUtil.unserializeList(data);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * list的长度
     *
     * @param key
     * @return
     * @throws Exception
     */
    private Long listLength(String key) {
        Jedis jedis = null;
        Long length = 0L;
        try {
            jedis = getResource();
            length = jedis.llen(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return length;
    }

    /**
     * list是否为空
     *
     * @param key
     * @return
     */
    private boolean isEmpty(String key) {
        Long l = listLength(key);
        if (l.equals(0L))
            return true;
        return false;
    }

    /**
     * 设置key的生存时间
     *
     * @param key
     */
    private void expire(String key, Integer second) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.expire(key, second);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 设置锁
     *
     * @param key
     * @param value
     * @throws Exception
     */
    public long setnx(String key, String value) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.setnx(key, value);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    public Map<String, Integer> getMapAll(String key) throws Exception {
        Map<String, String> result = null;
        Map<String, Integer> resultMap = new HashMap<>();
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.select(0);
            result = jedis.hgetAll(key);
            Set<String> keys = result.keySet();
            for (String k : keys) {
                resultMap.put(k, Integer.parseInt(result.get(k)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
        return resultMap;
    }

    //基础方法变型

    /**
     * 设置redis list
     *
     * @param key
     * @param start
     * @param end
     * @throws IllegalArgumentException
     */
    private void setNumberList(String key, Integer start, Integer end) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            int startNum = start.intValue();
            int endNum = end.intValue();
            for (int i = startNum; i <= endNum; i++) {
                jedis.lpush(key, String.valueOf(i));
            }
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 获取redis list的第一个值
     *
     * @param key
     * @return
     * @throws IllegalArgumentException
     */
    private Integer getNumberFromFront(String key) throws Exception {
        Integer result;
        Jedis jedis = null;
        try {
            jedis = getResource();
            result = Integer.valueOf(jedis.rpop(key));
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 加入值到某个list
     *
     * @param key
     * @param sequence
     * @throws JedisConnectionException
     * @throws IllegalArgumentException
     */
    private void addNumberToList(String key, Integer sequence) throws Exception {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.lpush(key, String.valueOf(sequence));
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 设置key的生存时间(以天数计算）
     *
     * @param key
     */
    private void expireDay(String key, Integer day) throws Exception {
        expire(key, day * 24 * 60 * 60);
    }


    //实现方法

    /**
     * 设置发票号段
     *
     * @param start
     * @param end
     * @throws UnsupportedOperationException
     */
    public void setInvoiceSerialsNumberList(Integer start, Integer end) throws UnsupportedOperationException {
        try {
            setNumberList(invoiceKey, start, end);
        } catch (Exception e) {
            throw new UnsupportedOperationException("redis");
        }
    }

    /**
     * 获得发票号段的第一个
     *
     * @return
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    public Integer getInvoiceSerialsNumberFromFront() throws IllegalArgumentException, UnsupportedOperationException {
        try {
            return getNumberFromFront(invoiceKey);
        } catch (Exception e) {
            throw new IllegalArgumentException("redis");
        }
    }

    /**
     * 设置挂号顺序
     *
     * @param id
     * @param amount
     * @throws IllegalArgumentException
     */
    public void setRegistrationSequenceList(Integer id, Integer amount) throws UnsupportedOperationException {
        try {
            setNumberList(id.toString() + registrationKey, 1, amount);
            expireDay(id.toString() + registrationKey, 3);
        } catch (Exception e) {
            throw new UnsupportedOperationException("redis");
        }
    }

    /**
     * 获得某个医生顺序号的第一个
     *
     * @param id
     * @return
     * @throws IllegalArgumentException
     */
    public Integer getRegistrationSequenceFromFront(Integer id) throws IndexOutOfBoundsException {
        try {
            return getNumberFromFront(id.toString() + registrationKey);
        } catch (Exception e) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * 退号时处理顺序号
     *
     * @param id
     * @param sequence
     * @throws UnsupportedOperationException
     */
    public void addRegistrationSequenceList(Integer id, Integer sequence) throws UnsupportedOperationException {
        try {
            addNumberToList(id.toString() + registrationKey, sequence);
        } catch (Exception e) {
            throw new UnsupportedOperationException("redis");
        }
    }

    /**
     * 挂号是否为空
     *
     * @param id
     * @return
     */
    public boolean isRegistrationEmpty(Integer id) {
        return isEmpty(id.toString() + registrationKey);
    }


    /**
     * 病历暂存
     *
     * @param id
     * @param record
     * @throws Exception
     */
    public void setTemporaryMedicalRecord(Integer id, MedicalRecord record) throws Exception {
        setObject(id.toString() + medicalRecordKey, record);
        expireDay(id.toString() + medicalRecordKey, 1);

    }

    /**
     * 获得暂存病历
     *
     * @param id
     * @return
     * @throws Exception
     */
    public MedicalRecord getTemporaryMedicalRecord(Integer id) throws Exception {
        return (MedicalRecord) getObject(id.toString() + medicalRecordKey);
    }

    public void deleteTemporaryMR(Integer id) throws Exception {
        Jedis jedis = getResource();
        try {
            jedis.del(id.toString() + medicalRecordKey);
        } catch (Exception e) {
            throw new Exception();
        } finally {
            returnResource(jedis);
        }
    }

    public void setTemporaryInspection(Integer id, List<InspectionApplication> applications) throws Exception {
        if (applications == null || applications.size() == 0) {
            deleteTemporaryInspection(id);
        } else {
            setObjectList(id.toString() + applicationKey, applications);
            expireDay(id.toString() + applicationKey, 1);
        }
    }

    public void setTemporaryInspectionDrug(Integer id, List<Prescription> prescriptions) throws Exception {
        if (prescriptions == null || prescriptions.size() == 0) {
            deleteTemporaryInspectionDrug(id);
        } else {
            setObjectList(id.toString() + prescriptionKey, prescriptions);
            expireDay(id.toString() + prescriptionKey, 1);
        }
    }

    /**
     * 获得检查
     *
     * @param id
     * @return
     * @throws Exception
     */
    public List<Prescription> getTemporaryPrescription(Integer id) throws Exception {
        ArrayList<Prescription> list = (ArrayList<Prescription>) getObjectList(id.toString() + prescriptionKey);
        return list;
    }

    public List<InspectionApplication> getTemporaryApplications(Integer id) throws Exception {
        return (ArrayList<InspectionApplication>) getObjectList(id.toString() + applicationKey);
    }

    public void deleteTemporaryInspection(Integer id) {
        Jedis jedis = getResource();
        try {
            jedis.del(id.toString() + applicationKey);
        } catch (Exception e) {
            //returnBrokenResource(jedis);
        } finally {
            returnResource(jedis);
        }
    }

    public void deleteTemporaryInspectionDrug(Integer id) {
        Jedis jedis = getResource();
        try {
            jedis.del(id.toString() + prescriptionKey);
        } catch (Exception e) {
            //returnBrokenResource(jedis);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 添加一条记录 如果map_key存在 则更新value
     * hset 如果哈希表不存在，一个新的哈希表被创建并进行 HSET 操作。
     * 如果字段已经存在于哈希表中，旧值将被覆盖
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    public long setHash(String key, String field, String value) {
        Jedis jedis = getResource();
        jedis.select(0);
        long returnStatus = jedis.hset(key, field, value);
        return returnStatus;
    }

    /**
     * 删除hash中 field对应的值
     * hdel 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略
     * @param key
     * @param field
     * @return
     */
    public long deleteHash(String key, String... field) {
        Jedis jedis = getResource();
        jedis.select(0);
        long returnStatus = jedis.hdel(key, field);
        return returnStatus;
    }

    public void setPaymentType(PaymentType type){
        Jedis jedis = getResource();

        try {
            setHash("paymentType",type.getName(),type.getType().toString());
        } catch (Exception e) {
            //returnBrokenResource(jedis);
        }finally {
            returnResource(jedis);
        }
    }

    public void deletePaymentType(PaymentType type){
        Jedis jedis = getResource();
        try {
            deleteHash("paymentType",type.getName(),type.getType().toString());
        } catch (Exception e) {
            //returnBrokenResource(jedis);
        }finally {
            returnResource(jedis);
        }
    }
}