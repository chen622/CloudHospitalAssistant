package cn.neuedu.his.service;

import cn.neuedu.his.model.Invoice;

import cn.neuedu.his.model.Payment;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface RegistrationService extends Service<Registration> {
    Payment registerRegistrationInfo(Integer registrarId, Integer patientId, Integer scheduleId, Boolean needBook) throws IllegalArgumentException, IndexOutOfBoundsException;
    Payment preRegistration(Integer patientId, Integer scheduleId);
    void confirmPre(Integer registrarId, Integer registrationId) throws IllegalArgumentException;
    Invoice retreatRegistrationInfo(Integer registrationId, Integer registrarId);
    List<Registration> getAllWaitingRegistration(Integer doctorID, Integer state, Date time);
    List<Registration> getHadRegistration(Integer doctorID, Integer state, Date time);
    List<Registration> getFinishRegistration(Integer doctorID, Integer state, Date time);
    List<Registration> getRegistrationByPatientName(String name,Integer doctorID,Integer state);
    ArrayList<Registration> findAllRegistrationWaitingByPatientId(Integer patientId);
    ArrayList<Integer> getAllByDoctor(Integer doctorId, String start, String end, Integer state);
    void setRegistrationSequence();
    Integer getRegistrationInof(Date time, Integer doctorId);
    Integer getRegistrationState(Integer id);
    Registration findRegistrationAndType(Integer id);
}
