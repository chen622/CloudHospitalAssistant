package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PatientMapper;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static cn.neuedu.his.util.constants.Constants.*;

/**
 * Created by ccm on 2019/05/26.
 */
@Service
public class PatientServiceImpl extends AbstractService<Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    /**
     * 查找患者(某时间段内)所有缴费信息
     * @param patientId
     * @param start
     * @param end
     * @return
     */
    @Override
    public Patient findAllPayment(Integer patientId, Date start, Date end) {
        Patient patient = patientMapper.getPatientAndAllPayment(patientId, start, end);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 查找未缴费缴费单（未冻结）
     *
     * @param patientId
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findNotPaidPayment(Integer patientId) throws IllegalArgumentException {
        Patient patient = patientMapper.getPatientAndPaymentByState(patientId, Constants.PRODUCE_PAYMENT);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 查找所有项目缴费单
     *
     * @param patientId
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findNotConsumePayment(Integer patientId) throws IllegalArgumentException {
        Patient patient = patientMapper.getPatientAndNotConsumePayment(patientId, Constants.REGISTRATION_PAYMENT_TYPE);
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
    public Patient findPatientAndNotTakeDrug(Integer patientId, Date start, Date end) throws IllegalArgumentException{
        Patient patient =  patientMapper.getPatientAndDrugByTypeAndState(patientId, Constants.DRUG_PAYMENT_TYPE, Constants.HAVE_PAID, start, end);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 查询相应的已取的药品信息
     * @param patientId
     * @param start
     * @param end
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findPatientAndTakenDrug(Integer patientId, Date start, Date end) throws IllegalArgumentException{
        Patient patient =  patientMapper.getPatientAndDrugByTypeAndState(patientId, Constants.DRUG_PAYMENT_TYPE, Constants.HAVE_COMPLETED_PAID, start, end);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }

    /**
     * 查询相应的退还的药品信息
     * @param patientId
     * @param start
     * @param end
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Patient findPatientAndReturnDrug(Integer patientId, Date start, Date end) throws IllegalArgumentException{
        Patient patient =  patientMapper.getPatientAndDrugByTypeAndState(patientId, Constants.DRUG_PAYMENT_TYPE, HAVE_RETURN_DRUG, start, end);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }




    /**
     * 根据身份证号模糊查询
     *
     * @param id
     * @return
     */
    @Override
    public List<Patient> selectPatientByIdentifyId(String id) {
        return patientMapper.selectPatientByIdentifyId(id);
    }

    /**
     * 根据真实姓名模糊查询
     *
     * @param name
     * @return
     */
    @Override
    public List<Patient> selectPatientByName(String name) {
        return patientMapper.selectPatientByName(name);
    }

    /**
     * 根据电话查找病人信息
     *
     * @param phoneNumber
     * @return
     */
    @Override
    public List<Patient> selectPatientByPhone(String phoneNumber) {
        return patientMapper.selectPatientByPhone(phoneNumber);
    }

    @Override
    public List<Patient> selectPatientByIdentifyIdAndNameAndPhone(String identifyId, String name, String phone) {
        return patientMapper.selectPatientByIdentifyIdAndNameAndPhone(identifyId, name, phone);
    }


}
