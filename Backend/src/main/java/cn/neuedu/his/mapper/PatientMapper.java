package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface PatientMapper extends MyMapper<Patient> {
    Patient searchPatientAndFrozenPayment(Integer patientId);
}