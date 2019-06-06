package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PatientMapper;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 *
 * Created by ccm on 2019/05/26.
 */
@Service
public class PatientServiceImpl extends AbstractService<Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    /**
     * 查找未缴费缴费单（未冻结）
     * @param patientId
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findPatientAndPaymentInfo(Integer patientId) throws IllegalArgumentException{
        Patient patient = patientMapper.searchPatientAndFrozenPayment(patientId, REGISTRATION_PAYMENT_TYPE, PRODUCE_PAYMENT);
        if (patient == null)
             throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 查找所有项目缴费单
     * @param patientId
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findPatientAndNotConsumePayment(Integer patientId) throws IllegalArgumentException{
        Patient patient = patientMapper.searchPatientAndNotConsumePayment(patientId, REGISTRATION_PAYMENT_TYPE, PRODUCE_PAYMENT, HAVE_PAID, HAVE_COMPLETED_PAID);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 查询相应的已缴费尚未发放的药品信息
     * @param patientId
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findPatientAndNotTakeDrug(Integer patientId) throws IllegalArgumentException{
        Patient patient = patientMapper.searchPatientAndNotTakeDrug(patientId, DRUG_PAYMENT_TYPE, HAVE_PAID);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 查询某时间段内患者的药物情况
     * @param patientId
     * @param startDate
     * @param endDate
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findPatientAndDrugDuringDate(Integer patientId, Date startDate, Date endDate) throws IllegalArgumentException{
        Patient patient = patientMapper.searchPatientAndDrugDuringDate(patientId, DRUG_PAYMENT_TYPE, startDate, endDate);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 根据身份证号模糊查询
     * @param id
     * @return
     */
    @Override
    public List<Patient> selectPatientByIdentifyId(String id) {
        return patientMapper.selectPatientByIdentifyId(id);
    }

    /**
     * 根据真实姓名模糊查询
     * @param name
     * @return
     */
    @Override
    public List<Patient> selectPatientByName(String name) {
        return patientMapper.selectPatientByName(name);
    }

    /**
     * 根据电话查找病人信息
     * @param phoneNumber
     * @return
     */
    @Override
    public List<Patient> selectPatientByPhone(String phoneNumber) {
        return patientMapper.selectPatientByPhone(phoneNumber);
    }


}
