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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @SuppressWarnings("deprecation")
    private void returnBrokenResource(Jedis jedis) {
        if (jedis != null) {
            jedisPool.returnBrokenResource(jedis);
        }
    }

    private void set(String key, String value) {
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.set(key, value);
            logger.info("Redis set success - " + key + ", value:" + value);
        } catch (Exception e) {
            e.printStackTrace();
            returnBrokenResource(jedis);
            logger.error("Redis set error: "+ e.getMessage() +" - " + key + ", value:" + value);
        }finally{
            returnResource(jedis);
        }
    }

    private String get(String key) {
        String result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            result = jedis.get(key);
            logger.info("Redis get success - " + key + ", value:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            returnBrokenResource(jedis);
            logger.error("Redis set error: "+ e.getMessage() +" - " + key + ", value:" + result);
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
            e.printStackTrace();
            returnBrokenResource(jedis);
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
            e.printStackTrace();
            returnBrokenResource(jedis);
            throw new  Exception();
        }finally{
            returnResource(jedis);
        }

        return result;
    }

    private void setObjectList(String key, List<?> list) {
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.set(key.getBytes(), SerializeUtil.serializeList(list));
        } catch (Exception e) {
            e.printStackTrace();
            returnBrokenResource(jedis);
        }finally{
            returnResource(jedis);
        }
    }

    private List<?> getObjectList(String key) {
        List<?> result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            byte[] data = jedis.get(key.getBytes());
            result = SerializeUtil.unserializeList(data);
        } catch (Exception e) {
            e.printStackTrace();
            returnBrokenResource(jedis);
        }finally{
            returnResource(jedis);
        }

        return result;
    }



    //实现方法
    /**
     * 设置redis list
     * @param key
     * @param start
     * @param end
     * @throws IllegalArgumentException
     */
    private void setNumberList(String key, Integer start, Integer end) throws IllegalArgumentException{
        Jedis jedis=null;
        try{
            jedis = getResource();
            int startNum = start.intValue();
            int endNum = end.intValue();
            for (int i = startNum; i <= endNum; i++) {
                jedis.lpush(key, String.valueOf(i));
            }
            logger.info("Redis set success - " + key + ", start:" + start + ", end:" + end);
        } catch (Exception e) {
            logger.error("Redis set error: "+ e.getMessage() +" - " + key + ", start:" + start + ", end:" + end);
            returnBrokenResource(jedis);
            throw new IllegalArgumentException();
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
    private Integer getNumberFromFront(String key) throws IllegalArgumentException{
        Integer result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            result = Integer.valueOf(jedis.rpop(key));
            logger.info("Redis get success - " + key + ", value:" + result);
        } catch (Exception e) {
            logger.error("Redis set error: "+ e.getMessage() +" - " + key + ", value:" + result);
            returnBrokenResource(jedis);
            throw new IllegalArgumentException();
        }finally{
            returnResource(jedis);
        }

        return result;
    }

    private void addNumberToList(String key, Integer sequence) throws IllegalArgumentException{
        Jedis jedis=null;
        try{
            jedis = getResource();
            jedis.lpush(key, String.valueOf(sequence));
            logger.info("Redis set success - " + key + ", sequence" + sequence);
        } catch (Exception e) {
            logger.error("Redis set error: "+ e.getMessage() +" - " + key + ", sequence" + sequence);
            returnBrokenResource(jedis);
            throw new IllegalArgumentException();
        }finally{
            returnResource(jedis);
        }
    }







    /**
     * 设置发票号段
     * @param start
     * @param end
     * @throws IllegalArgumentException
     */
    public void setInvoiceSerialsNumberList(Integer start, Integer end) throws IllegalArgumentException{
        try {
            setNumberList(invoiceKey, start, end);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }

    }

    /**
     * 获得发票号段的第一个
     * @return
     */
    public Integer getInvoiceSerialsNumberFromFront() {
        try {
            return getNumberFromFront(invoiceKey);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 设置挂号顺序
     * @param id
     * @param amount
     * @throws IllegalArgumentException
     */
    public void setRegistrationSequenceList(Integer id, Integer amount) throws IllegalArgumentException{
        try {
            setNumberList(id.toString() + "-" + registrationKey, 0, amount);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
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
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * 退号时处理顺序号
     * @param id
     * @param sequence
     * @throws IllegalArgumentException
     */
    public void addRegistrationSequenceList(Integer id, Integer sequence) throws IllegalArgumentException {
        try {
            addNumberToList(id.toString() + "-" + registrationKey, sequence);
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
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
            returnBrokenResource(jedis);
        }finally {
            returnResource(jedis);
        }

    }

}