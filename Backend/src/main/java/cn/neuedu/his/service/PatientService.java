package cn.neuedu.his.service;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.util.inter.Service;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by ccm on 2019/05/26.
 */
public interface PatientService extends Service<Patient> {
    Patient findPatientAndPaymentInfo(Integer patientId);
    Patient findPatientAndNotConsumePayment(Integer patientId);
    Patient findPatientAndNotTakeDrug(Integer patientId);
    Patient findPatientAndDrugDuringDate(Integer patientId, Date startDate, Date endDate) throws IllegalArgumentException;

    //根据身份证号模糊查询获得病人信息
    List<Patient> selectPatientByIdentifyId(String identifyId);
    //根据真实姓名模糊查询获得病人信息
    List<Patient> selectPatientByName(String name);
    //根据电话查找病人信息
    List<Patient> selectPatientByPhone(String phoneNumber);
}
