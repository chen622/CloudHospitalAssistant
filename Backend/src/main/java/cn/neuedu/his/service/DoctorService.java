package cn.neuedu.his.service;
import cn.neuedu.his.model.*;
import cn.neuedu.his.util.inter.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DoctorService extends Service<Doctor> {


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
    public JSONObject setFirstDiagnose(Integer registrationID, MedicalRecord medicalRecord, List<Integer> diagnoses) throws Exception;

    @Transactional
    public JSONObject saveMRTemplate(MedicalRecord record,Integer doctorID,String name,Integer level) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    @Transactional
    JSONObject openInspection(Integer registrationId);

    @Transactional
    JSONObject saveInspection(JSONObject object,Boolean isDisposal)throws  Exception;

    @Transactional
    public JSONObject saveInspectionTemplate(JSONObject object,Integer level,Integer doctorId) throws Exception;

    @Transactional
    public JSONObject saveInspectionAsTemplate(JSONObject object,Integer level,Integer doctorId) throws Exception;

    @Transactional
    JSONObject getInspectionResult(Integer id);

    @Transactional
    JSONObject saveFinalDiagnose(Integer registrationId,Integer medicalRecordId,List<Integer> diagnoses) throws Exception;
}

