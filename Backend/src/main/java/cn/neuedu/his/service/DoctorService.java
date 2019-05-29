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
    public JSONObject getHospitalMR(Integer doctorID,Integer level) ;

    @Transactional
    public JSONObject getDeptMR(Integer doctorID,Integer level) ;

    @Transactional
    public JSONObject getPersonalMR(Integer doctorID,Integer level);

    @Transactional
    Integer getDeptNo(Integer id);

    @Transactional
    JSONObject findDiseaseByName(String name);

    @Transactional
    JSONObject getAllDiease();

    @Transactional
    JSONObject findNonDrugByName(String name);

    @Transactional
    JSONObject  getAllNonDrug();



    @Transactional
    public JSONObject setFirstDiagnose(Integer registrationID, MedicalRecord medicalRecord, Diagnose diagnose) throws Exception;

    @Transactional
    public JSONObject saveHospitalMRTemplate(MedicalRecord record,Integer doctorID,String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    @Transactional
    public JSONObject saveDeptMRTemplate(MedicalRecord record,Integer doctorID,String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;

    @Transactional
    public JSONObject savePersonalMRTemplate(MedicalRecord record,Integer doctorID,String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException;



}

