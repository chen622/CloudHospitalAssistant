package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.RegistrationMapper;
import cn.neuedu.his.model.JobSchedule;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class RegistrationServiceImpl extends AbstractService<Registration> implements RegistrationService {
    @Autowired
    private RegistrationMapper registrationMapper;
    @Autowired
    private PatientService patientService;
    @Autowired
    private JobScheduleService jobScheduleService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RegistrationTypeService registrationTypeService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private RedisServiceImpl redisService;

    /**
     * 现场挂号
     * @param registrarId
     * @param jsonObject
     * @throws IllegalArgumentException
     */
    @Transactional
    @Override
    public void registerRegistrationInfo(Integer registrarId, JSONObject jsonObject) throws IllegalArgumentException, IndexOutOfBoundsException{
        //获取挂号信息
        Registration registration = new Registration();
        registration.setRegistrarId(registrarId);
        Patient patient = patientService.findById(jsonObject.getInteger("patientId"));
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        registration.setPatientId(patient.getId());
        //获取患者年龄
        try {
            registration.setAge(identityIdTransferToAge(patient.getIdentityId()));
        }catch (ParseException | InvalidParameterException e) {
            throw new IllegalArgumentException("IdentityId");
        }

        //获取时间表内信息
        JobSchedule schedule = jobScheduleService.findById(jsonObject.getInteger("scheduleId"));
        if (schedule == null)
            throw new IllegalArgumentException("scheduleId");

        registration.setScheduleId(schedule.getId());
        registration.setDoctorId(schedule.getDoctorId());
        registration.setState(WAITING_FOR_TREATMENT);
        registration.setNeedBook(jsonObject.getBoolean("needBook"));
        //从redis中获取顺序号
        try {
            registration.setSequence(redisService.getRegistrationSequenceFromFront(schedule.getId()));
        }catch (IllegalArgumentException e) {
            throw new IndexOutOfBoundsException("sequence");
        }
        registration.setSerialNumber(1);

        save(registration);

        //改变已挂号人数
        jobScheduleService.addHaveRegistrationAmount(schedule.getId());

        //生成缴费单
        BigDecimal unitPrice = registrationTypeService.findById(schedule.getRegistrationTypeId()).getPrice();
        Integer paymentId = paymentService.createRegistrationPayment(registration, jsonObject.getInteger("settlementType"), unitPrice);

        //生成发票
        try {
            invoiceService.addInvoiceByPayment(paymentService.findById(paymentId));
        }catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("invoice");
        }
    }

    /**
     * 通过身份证获取年龄
     * @param identifyId
     * @return
     * @throws ParseException
     * @throws InvalidParameterException
     */
    private Integer identityIdTransferToAge(String identifyId) throws ParseException, InvalidParameterException {
        //通过身份证获取生日
        String birthdayStr = identifyId.substring(6, 14);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Date birthday;
        try {
            birthday = format.parse(birthdayStr);
        } catch (ParseException e) {
            throw e;
        }
        System.out.println(birthday);

        ////通过生日获取年龄
        Calendar calendar = Calendar.getInstance();
        if(calendar.getTimeInMillis() - birthday.getTime() < 0L)
            throw new InvalidParameterException();

        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(birthday);
        int yearBirthday = calendar.get(Calendar.YEAR);
        int monthBirthday = calendar.get(Calendar.MONTH);
        int dayOfMonthBirthday = calendar.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirthday;

        if (monthNow < monthBirthday || (monthNow == monthBirthday && dayOfMonthNow < dayOfMonthBirthday)) {
            age--;
        }

        return age;
    }

    /**
     * 退号
     * @param registrationId
     * @param registrarId
     * @throws RuntimeException
     */
    @Transactional
    @Override
    public void retreatRegistrationInfo(Integer registrationId, Integer registrarId) throws UnsupportedOperationException, IllegalArgumentException{
        Registration registration = findById(registrationId);
        if (registration == null)
            throw new IllegalArgumentException("registrationId");
        Integer state = registration.getState();
        if (!state.equals(RESERVATION) && !state.equals(WAITING_FOR_TREATMENT))
            throw new UnsupportedOperationException("503");

        registration.setState(CANCEL);
        update(registration);
        redisService.addRegistrationSequenceList(registration.getScheduleId(), registration.getSequence());

        //改变已挂号人数
        jobScheduleService.reduceRegistrationAmount(registration.getScheduleId());

        //形成冲红缴费单及发票
        Integer paymentId = paymentService.findByRegistrationId(registrationId).getId();
        paymentService.produceRetreatPayment(paymentId, registrarId, 1);
    }

    /**
     * 查找所有带诊患者
     * @param doctorID
     * @param state
     * @return
     */
    public List<Registration> getAllWaitingRegistration(Integer doctorID,Integer state,Date time){
        return registrationMapper.getAllWaitingRegistration(doctorID,state,time);
    }

    /**
     * 通过用户名查找所有带诊患者
     * @param name
     * @param doctorID
     * @param state
     * @return
     */
    public  List<Registration> getRegistrationByPatientName(String name,Integer doctorID,Integer state){
        return registrationMapper.getRegistrationByPatientName(name,doctorID, Constants.WAITING_FOR_TREATMENT);
    }

    @Override
    public ArrayList<Registration> findAllRegistrationWaitingByPatientId(Integer patientId) {
        return registrationMapper.getAllRegistrationWaitingByPatientId(patientId, RESERVATION, WAITING_FOR_TREATMENT);
    }


}
