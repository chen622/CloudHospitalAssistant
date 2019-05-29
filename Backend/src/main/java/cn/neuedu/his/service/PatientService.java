package cn.neuedu.his.service;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.util.inter.Service;

/**
 *
 * Created by ccm on 2019/05/26.
 */
public interface PatientService extends Service<Patient> {
    Patient findPatientAndPaymentInfo(Integer patientId);
    Patient findPatientAndNotConsumePayment(Integer patientId);
}
