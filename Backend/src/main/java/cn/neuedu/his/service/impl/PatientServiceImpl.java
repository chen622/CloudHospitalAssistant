package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PatientMapper;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Patient patient = patientMapper.searchPatientAndNotConsumePayment(patientId, REGISTRATION_PAYMENT_TYPE);
        if (patient == null)
            throw new IllegalArgumentException("patientId");

        return patient;
    }
}
