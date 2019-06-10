package cn.neuedu.his.service;
import cn.neuedu.his.model.*;
import cn.neuedu.his.util.inter.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.beans.Transient;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DoctorService extends Service<Doctor> {

    @Transactional
     void saveDiagnose(List<Integer> diagnoses ,Integer itemId,Boolean isMajor,Boolean isTemplate);

    @Transient
    JSONObject updateMR(MedicalRecord record,List<Integer> diagnoses,List<Integer> finalDiagnose) ;

    @Transactional
    JSONObject getRegistrationInof(Date time,Integer doctorId);

    @Transactional
    public JSONObject getHospitalCheckTemps(Integer doctorID,Integer level);

    @Transactional
    public JSONObject getDeptCheckTemps(Integer doctorID,Integer level);

    @Transactional
    public JSONObject getPersonalCheckTemps(Integer doctorID,Integer level);

    @Transactional
    public JSONObject getHospitalMR(Integer doctorID, Integer level) ;

    @Transactional
    public JSONObject getDeptMR(Integer doctorID,Integer level) ;

    @Transactional
    public JSONObject getPersonalMR(Integer doctorID, Integer level);

    @Transactional
    JSONObject getMeicalRecordTemByName(String name);

    @Transactional
    Integer getDeptNo(Integer id);

    @Transactional
    JSONObject findDiseaseByName(String name);

    @Transactional
    JSONObject getAllDiease();

    @Transactional
    List<NonDrug> findNonDrugByName(String name);

    @Transactional
    JSONObject  getAllNonDrug();



    @Transactional
    public JSONObject setFirstDiagnose(Integer registrationID, MedicalRecord medicalRecord, List<Integer> diagnoses,Integer doctorId) throws Exception;

    @Transactional
    public JSONObject saveMRTemplate(MedicalRecord record,Integer doctorID,String name,Integer level) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    @Transactional
    public JSONObject deleteMedicalRecordTemp(Integer id,Integer doctorId) ;

    @Transactional
    public JSONObject updateMedicalRecordTem(MedicalRecordTemplate record, Integer doctorID);
    @Transactional
    JSONObject openInspection(Integer registrationId);

    @Transactional
    JSONObject saveInspection(JSONObject object,Boolean isDisposal,Integer doctorId)throws  Exception;

    @Transactional
    public JSONObject saveInspectionAsTemplate(InspectionTemplate template ,Integer doctorId) throws Exception;

    @Transactional
    public JSONObject updateInspectionTem(InspectionTemplate template ,Integer doctorId) throws Exception;


    @Transactional
    JSONObject getInspectionTemByName(String name);

    @Transactional
    public JSONObject deleteInspectionTemp(Integer id,Integer doctorId) ;


    @Transactional
    JSONObject getInspectionResult(Integer id);

    @Transactional
    JSONObject saveFinalDiagnose(Integer registrationId,Integer medicalRecordId,List<Integer> diagnoses) throws Exception;

    @Transactional
    public JSONObject savePrescriptions(List<Prescription> prescriptions,Integer medicalRecordId,Integer registationId,Integer doctorId) throws Exception;

    @Transactional
    public JSONObject savePrescriptionsTemp(DrugTemplate template,Integer medicalRecordId,Integer doctorId) throws Exception;

    @Transactional
    JSONObject getPrescriptionsTemByName(String name);

    @Transactional
    public  JSONObject finishDiagnose(Integer registrationId);

    @Transactional
    public JSONObject getAllPaymentDetails(Integer medicalRecordIds,Integer registrationId);

    @Transactional
    public JSONObject getDoctorTotal(Integer doctorId,String start,String end);

    @Transactional
    public Integer registrationNum(Integer doctorId, String start, String end);

    JSONArray doctorWorkCalculate(Date startDate, Date endDate);
}

