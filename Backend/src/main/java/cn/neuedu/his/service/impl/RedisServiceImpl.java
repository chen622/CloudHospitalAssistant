package cn.neuedu.his.service.impl;

import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.Prescription;
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

@Service
public class RedisServiceImpl{
    private static Logger logger = Logger.getLogger(RedisServiceImpl.class);

    @Autowired
    private JedisPool jedisPool;    //jedisPool不属于springboot框架支持的redis类,所以需要自行注入到spring中。通过配置类RedisConfig类注入的

    private String invoiceKey = "invoice-list";
    private String registrationKey = "registration-list";

    //基础方法
    private Jedis getResource() {
        return jedisPool.getResource();
    }

    @SuppressWarnings("deprecation")
    private void returnResource(Jedis jedis) {
        if(jedis != null)
            jedisPool.returnResourceObject(jedis);
    }

    private void set(String key, String value) throws Exception{
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.set(key, value);
        } catch (Exception e) {
            throw new Exception();
        }finally{
            returnResource(jedis);
        }
    }

    private String get(String key) throws Exception{
        String result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            throw new Exception();
        }finally{
            returnResource(jedis);
        }

        return result;
    }

    private void setObject(String key, Object value) throws Exception{
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            throw new Exception();
        }finally{
            returnResource(jedis);
        }
    }

    private Object getObject(String key ) throws Exception{
        Object result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            byte[] data = jedis.get(key.getBytes());
            result = SerializeUtil.unserialize(data);
        } catch (Exception e) {
            throw new  Exception();
        }finally{
            returnResource(jedis);
        }

        return result;
    }

    private void setObjectList(String key, List<?> list) throws Exception{
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.set(key.getBytes(), SerializeUtil.serializeList(list));
        } catch (Exception e) {
           throw new Exception();
        }finally{
            returnResource(jedis);
        }
    }

    private List<?> getObjectList(String key) throws Exception{
        List<?> result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            byte[] data = jedis.get(key.getBytes());
            result = SerializeUtil.unserializeList(data);
        } catch (Exception e) {
            throw new Exception();
        }finally{
            returnResource(jedis);
        }

        return result;
    }


    public  Map<String, Integer> getMapAll(String key ) throws Exception{
        Map<String, String> result = null;
        Map<String, Integer> resultMap= new HashMap<>();
        Jedis jedis=null;
        try{
            jedis = getResource();
            result = jedis.hgetAll(key);
            Set<String> keys=result.keySet();
            for (String  k:keys){
                resultMap.put(k,Integer.parseInt(result.get(k)));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new  Exception();
        }finally{
            returnResource(jedis);
        }
        return resultMap;
    }

    //实现方法
    /**
     * 设置redis list
     * @param key
     * @param start
     * @param end
     * @throws IllegalArgumentException
     */
    private void setNumberList(String key, Integer start, Integer end) throws Exception{
        Jedis jedis=null;
        try{
            jedis = getResource();
            int startNum = start.intValue();
            int endNum = end.intValue();
            for (int i = startNum; i <= endNum; i++) {
                jedis.lpush(key, String.valueOf(i));
            }
        } catch (Exception e) {
            throw new Exception();
        }finally{
            returnResource(jedis);
        }
    }

    /**
     * 获取redis list的第一个值
     * @param key
     * @return
     * @throws IllegalArgumentException
     */
    private Integer getNumberFromFront(String key) throws Exception{
        Integer result;
        Jedis jedis=null;
        try{
            jedis = getResource();
            result = Integer.valueOf(jedis.rpop(key));
        } catch (Exception e) {
            throw new Exception();
        }finally{
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 加入值到某个list
     * @param key
     * @param sequence
     * @throws JedisConnectionException
     * @throws IllegalArgumentException
     */
    private void addNumberToList(String key, Integer sequence) throws Exception{
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.lpush(key, String.valueOf(sequence));
        } catch (Exception e) {
            throw new Exception();
        }finally{
            returnResource(jedis);
        }
    }


    /**
     * 设置发票号段
     * @param start
     * @param end
     * @throws UnsupportedOperationException
     */
    public void setInvoiceSerialsNumberList(Integer start, Integer end) throws UnsupportedOperationException{
        try {
            setNumberList(invoiceKey, start, end);
        }catch (Exception e) {
            throw new UnsupportedOperationException("redis");
        }
    }

    /**
     * 获得发票号段的第一个
     * @return
     * @throws IllegalArgumentException
     * @throws UnsupportedOperationException
     */
    public Integer getInvoiceSerialsNumberFromFront() throws IllegalArgumentException, UnsupportedOperationException{
        try {
            return getNumberFromFront(invoiceKey);
        }catch (Exception e) {
            throw new IllegalArgumentException("redis");
        }
    }

    /**
     * 设置挂号顺序
     * @param id
     * @param amount
     * @throws IllegalArgumentException
     */
    public void setRegistrationSequenceList(Integer id, Integer amount) throws UnsupportedOperationException{
        try {
            setNumberList(id.toString() + "-" + registrationKey, 1, amount);
        }catch (Exception e) {
            throw new UnsupportedOperationException("redis");
        }
    }

    /**
     * 获得某个医生顺序号的第一个
     * @param id
     * @return
     * @throws IllegalArgumentException
     */
    public Integer getRegistrationSequenceFromFront(Integer id) throws IllegalArgumentException{
        try {
            return getNumberFromFront(id.toString() + "-" + registrationKey);
        }catch (Exception e) {
            throw new IllegalArgumentException("redis");
        }
    }

    /**
     * 退号时处理顺序号
     * @param id
     * @param sequence
     * @throws UnsupportedOperationException
     */
    public void addRegistrationSequenceList(Integer id, Integer sequence) throws UnsupportedOperationException {
        try {
            addNumberToList(id.toString() + "-" + registrationKey, sequence);
        }catch (Exception e) {
            throw new UnsupportedOperationException("redis");
        }
    }


    /**
     * 病历暂存
     * @param id
     * @param record
     * @throws Exception
     */
    public void setTemporaryMedicalRecord(Integer id, MedicalRecord record) throws Exception {
        setObject(id.toString()+"MR", record);
    }

    /**
     * 获得暂存病历
     * @param id
     * @return
     * @throws Exception
     */
    public MedicalRecord getTemporaryMedicalRecord(Integer id) throws Exception {
        return (MedicalRecord) getObject(id.toString()+"MR");
    }

    public void deleteTemporaryMR(Integer id) throws Exception {
        Jedis jedis=getResource();
        try{
            jedis.del(id.toString()+"MR");
        }catch (Exception e){
            throw  new Exception();
        }finally {
            returnResource(jedis);
        }
    }


    public void setTemporaryInspection(Integer id, List<InspectionApplication> applications, List<Prescription> prescriptions) throws Exception {
        setObjectList(id.toString()+"applications", applications);
        setObjectList(id.toString()+"prescriptions", prescriptions);
    }

    /**
     * 获得检查
     * @param id
     * @return
     * @throws Exception
     */
    public List<Prescription> getTemporaryPrescription(Integer id) throws Exception {
        ArrayList<Prescription> list= (ArrayList<Prescription>) getObjectList(id.toString()+"prescriptions");
        return  list;
    }

    public List<InspectionApplication> getTemporaryApplications(Integer id) throws Exception {
        return (ArrayList<InspectionApplication>)getObjectList(id.toString()+"applications");
    }

    public void  deleteTemporaryInspection(Integer id){
        Jedis jedis=getResource();
        try{
            jedis.del(id.toString()+"applications");
            jedis.del(id.toString()+"prescriptions");
        }catch (Exception e) {
            //returnBrokenResource(jedis);
        }finally {
            returnResource(jedis);
        }
    }



}