package cn.neuedu.his.service;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.util.inter.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by ccm on 2019/05/26.
 */
public interface PatientService extends Service<Patient> {
    Patient findAllPayment(Integer patientId, Date start, Date end);
    Patient findNotPaidPayment(Integer patientId);
    Patient findNotConsumePayment(Integer patientId);
    Patient findPatientAndNotTakeDrug(Integer patientId, Date start, Date end);
    Patient findPatientAndTakenDrug(Integer patientId, Date start, Date end);
    Patient findPatientAndReturnDrug(Integer patientId, Date start, Date end);

    List<Patient> selectPatientByIdentifyIdAndNameAndPhone(String identifyId,String name, String phone);

    //根据身份证号模糊查询获得病人信息
    List<Patient> selectPatientByIdentifyId(String identifyId);

    //根据真实姓名模糊查询获得病人信息
    List<Patient> selectPatientByName(String name);

    //根据电话查找病人信息
    List<Patient> selectPatientByPhone(String phoneNumber);
}
