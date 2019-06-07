package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.RegistrationMapper;
import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.StringUtils;
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
    private RedisServiceImpl redisService;

    /**
     * 将两天后挂号顺序号加入redis
     */
    @Override
    public void setRegistrationSequence() {
        ArrayList<JobSchedule> jobScheduleList = jobScheduleService.getAfterThreeDays();
        for (JobSchedule jobSchedule: jobScheduleList) {
            redisService.setRegistrationSequenceList(jobSchedule.getId(), jobSchedule.getLimitRegistrationAmount());
        }
    }

    /**
     * 现场挂号
     * @param registrarId
     * @param patientId
     * @param scheduleId
     * @param needBook
     * @return
     * @throws IllegalArgumentException
     * @throws IndexOutOfBoundsException
     * @throws UnsupportedOperationException
     */
    @Transactional
    @Override
    public Payment registerRegistrationInfo(Integer registrarId, Integer patientId, Integer scheduleId, Boolean needBook) throws IllegalArgumentException, IndexOutOfBoundsException, UnsupportedOperationException{
        //获取挂号信息
        Registration registration = new Registration();
        registration.setRegistrarId(registrarId);
        Patient patient = patientService.findById(patientId);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        registration.setPatientId(patient.getId());
        //获取患者年龄
        try {
            registration.setAge(StringUtils.identityIdTransferToAge(patient.getIdentityId()));
        }catch (ParseException | InvalidParameterException e) {
            throw new IllegalArgumentException("IdentityId");
        }

        //获取时间表内信息
        JobSchedule schedule = jobScheduleService.findById(scheduleId);
        if (schedule == null)
            throw new IllegalArgumentException("scheduleId");

        registration.setScheduleId(schedule.getId());
        registration.setDoctorId(schedule.getDoctorId());
        registration.setState(Constants.WAITING_FOR_TREATMENT);
        registration.setNeedBook(needBook);
        //从redis中获取顺序号
        try {
            registration.setSequence(redisService.getRegistrationSequenceFromFront(schedule.getId()));
        }catch (IllegalArgumentException e) {
            throw new IndexOutOfBoundsException("sequence");
        }
        //todo:设置挂号码（serialNumber）
        registration.setSerialNumber(1);
        save(registration);

        //若去完号码后队列为空，则更新代码
        if (redisService.isRegistrationEmpty(schedule.getId())) {
            schedule.setIsValid(false);
            jobScheduleService.update(schedule);
        }

        Integer paymentId;
        try {
            paymentId = paymentService.createRegistrationPayment(registration.getId());
        }catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        } catch (UnsupportedOperationException e) {
            throw new UnsupportedOperationException(e.getMessage());
        }

        return paymentService.findById(paymentId);
    }

    /**
     * 退号
     * @param registrationId
     * @param registrarId
     * @return
     * @throws UnsupportedOperationException
     * @throws IllegalArgumentException
     */
    @Transactional
    @Override
    public Invoice retreatRegistrationInfo(Integer registrationId, Integer registrarId) throws UnsupportedOperationException, IllegalArgumentException{
        Registration registration = findById(registrationId);
        if (registration == null)
            throw new IllegalArgumentException("registrationId");
        Integer state = registration.getState();
        if (!state.equals(RESERVATION) && !state.equals(WAITING_FOR_TREATMENT))
            throw new UnsupportedOperationException("503");

        registration.setState(CANCEL);
        update(registration);
        redisService.addRegistrationSequenceList(registration.getScheduleId(), registration.getSequence());

        //若该schedule被设为invalid，则改变
        if (redisService.isRegistrationEmpty(registration.getScheduleId())) {
            JobSchedule schedule = jobScheduleService.findById(registration.getScheduleId());
            if (schedule.getIsValid().equals(false)) {
                schedule.setIsValid(true);
                jobScheduleService.update(schedule);
            }
        }

        //形成冲红缴费单及发票
        Integer paymentId = paymentService.findByRegistrationId(registrationId).getId();
        Invoice invoice = paymentService.retreatPayment(paymentId, registrarId, 1);

        return invoice;
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

    @Override
    public List<Registration> getHadRegistration(Integer doctorID, Integer state, Date time) {
        return null;
    }

    @Override
    public List<Registration> getFinishRegistration(Integer doctorID, Integer state, Date time) {
        return null;
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

    @Override
    public ArrayList<Integer> getAllByDoctor(Integer doctorId, String start, String  end, Integer state) {
        return registrationMapper.getAllByDoctor(doctorId, start, end,state);
    }

    @Override
    public Integer getRegistrationInof(Date time, Integer doctorId) {
        return registrationMapper.getRegistrationInof(time, doctorId);
    }

    @Override
    public Integer getRegistrationState(Integer id) {
        return registrationMapper.getRegistrationState(id);
    }
}
