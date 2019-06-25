package cn.neuedu.his.service;

import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.util.inter.Service;

import java.util.ArrayList;

/**
 *
 * Created by ccm on 2019/05/29.
 */
public interface PrescriptionService extends Service<Prescription> {
    public  void deleteByTemplateId(Integer templateId);
    ArrayList<Prescription> getByMedicalRecordId(Integer id);
    Prescription findPrescriptionAndDrug(Integer id);
    ArrayList<Prescription> findPrescriptionAndDrugByMedical(Integer medicalId);

}
