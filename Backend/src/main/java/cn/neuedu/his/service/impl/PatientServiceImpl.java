package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.PatientMapper;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/26.
 */
@Service
public class PatientServiceImpl extends AbstractService<Patient> implements PatientService {

    @Autowired
    private PatientMapper patientMapper;

    @Override
    public Patient findPatientAndPaymentInfo(Integer patientId) {
        return patientMapper.searchPatientAndFrozenPayment(patientId);
    }
}
