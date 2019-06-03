package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DoctorMapper;
import cn.neuedu.his.model.*;
import cn.neuedu.his.service.*;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.Constants;
import cn.neuedu.his.util.constants.ErrorEnum;
import cn.neuedu.his.util.inter.AbstractService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.krb5.internal.PAData;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DoctorServiceImpl extends AbstractService<Doctor> implements DoctorService {

    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    MedicalRecordService medicalRecordService;
    @Autowired
    MedicalRecordTemplateService medicalRecordTemplateService;
    @Autowired
    DiagnoseService diagnoseService;
    @Autowired
    NonDrugService nonDrugService;
    @Autowired
    InspectionTemplateService inspectionTemplateService;
    @Autowired
    InspectionTemplateRelationshipService inspectionTemplateRelationshipService;
    @Autowired
    DrugTemplateService drugTemplateService;
    @Autowired
    DiseaseSecondService diseaseSecondService;
    @Autowired
    InspectionApplicationService inspectionApplicationService;
    @Autowired
    InspectionResultService resultService;
    @Autowired
    PaymentService paymentService;
    @Autowired
    DrugService drugService;
    @Autowired
    PrescriptionService prescriptionService;
    @Autowired
    JobScheduleService scheduleService;


    @Override
    public JSONObject getRegistrationInof(Date time, Integer doctorId) {
        return CommonUtil.successJson(scheduleService.getRegistrationInof(time,doctorId));
    }

    /**
     * 获得全院检查模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    @Transactional
    public JSONObject getHospitalCheckTemps(Integer doctorID,Integer level) {
        List<InspectionTemplate> templates=inspectionTemplateService.getHospitalCheckTemps(doctorID,level);
        if(templates==null)
            templates=new ArrayList<>();
        return CommonUtil.successJson(templates);
    }

    /**
     * 获得科室检查模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    @Transactional
    public JSONObject getDeptCheckTemps(Integer doctorID,Integer level) {
//        Registration registration = registrationService.findById(registrationId);
//        if(!registration.getState().equals(Constants.FIRST_DIAGNOSIS)){
//            return  CommonUtil.errorJson(ErrorEnum.E_601.addErrorParamName("registration state"));
//        }
        List<InspectionTemplate> templates=inspectionTemplateService.getDeptCheckTemps(doctorID,level);
        if(templates==null)
            templates=new ArrayList<>();
        return CommonUtil.successJson(templates);
    }

    /**
     * 获得个人检查模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    @Transactional
    public JSONObject getPersonalCheckTemps(Integer doctorID,Integer level) {
        List<InspectionTemplate> templates=inspectionTemplateService.getPersonalCheckTemps(doctorID,level);
        if(templates==null)
            templates=new ArrayList<>();
        return CommonUtil.successJson(templates);
    }


    /**
     * 获得全院病例模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    @Transactional
    public JSONObject getHospitalMR(Integer doctorID,Integer level) {
        List<MedicalRecordTemplate> templates=medicalRecordTemplateService.getHospitalMR(doctorID,level);
        return CommonUtil.successJson(templates);
    }

    /**
     * 获得科室病例模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    @Transactional
    public JSONObject getDeptMR(Integer doctorID,Integer level) {
        List<MedicalRecordTemplate> templates=medicalRecordTemplateService.getDeptMR(doctorID,level);
        return CommonUtil.successJson(templates);
    }

    /**
     * 获得个人病例模板
     * @param doctorID
     * @param level
     * @return
     */
    @Override
    @Transactional
    public JSONObject getPersonalMR(Integer doctorID,Integer level) {
        List<MedicalRecordTemplate> templates=medicalRecordTemplateService.getPersonalMR(doctorID,level);
        return CommonUtil.successJson(templates);
    }

    @Override
    public JSONObject getMeicalRecordTemByName(String name) {
        return CommonUtil.successJson(doctorService.getMeicalRecordTemByName(name));
    }


    @Override
    public Integer getDeptNo(Integer id) {
        return doctorMapper.getDeptNo(id);
    }

    /**
     * 通过部分连续的字段获得所有疾病
     * @param name
     * @return
     */
    @Override
    public JSONObject findDiseaseByName(String name) {
        List<DiseaseSecond> list=diseaseSecondService.findByName(name);
        if(list==null)
            list=new ArrayList<>();
        return CommonUtil.successJson(list);
    }

    /**
     * 获得所有疾病
     * @return
     */
    @Override
    public JSONObject getAllDiease() {
        List<DiseaseSecond> list=diseaseSecondService.getAll();
        if(list==null)
            list=new ArrayList<>();
        return CommonUtil.successJson(list);
    }

    /**
     * 通过部分连续的字段获得所有非药项目
     * @param name
     * @return
     */
    @Override
    public List<NonDrug> findNonDrugByName(String name) {
        return nonDrugService.findByName(name);
    }

    @Override
    public JSONObject getAllNonDrug() {
        return nonDrugService.getAll();
    }



    /**
     * 提交初诊信息
     * @param registrationID
     * @param medicalRecord
     * @return
     */
    @Override
    @Transactional
    public JSONObject setFirstDiagnose(Integer registrationID, MedicalRecord medicalRecord,List<Integer> diagnoses,Integer doctorId) throws Exception {
        JobSchedule schedule=scheduleService.getByDoctorId(doctorId, new Date(System.currentTimeMillis()));
        if (schedule.getHaveRegistrationAmount()+1>schedule.getLimitRegistrationAmount())
            return CommonUtil.errorJson(ErrorEnum.E_708.addErrorParamName(schedule.getLimitRegistrationAmount().toString()));

        Registration registration = registrationService.findById(registrationID);
        if(registration==null){
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        } else{
            registration.setState(Constants.FIRST_DIAGNOSIS);
            registrationService.update(registration);
        }
        //检查是否已经有初诊了
        MedicalRecord record =medicalRecordService.getByRegistrationId(registrationID);
       if(record.getFirstDiagnose()!=null && !record.getFirstDiagnose().isEmpty())
            return  CommonUtil.errorJson(ErrorEnum.E_615.addErrorParamName("firstDiagnose"));
        //检查是否有必要的参数没有填写完
        String  check=cheakMedicalRecord(medicalRecord);
        if(!check.equals("")){
            throw new RuntimeException("medicalRecord");
        }
        medicalRecord.setId(null);
        medicalRecordService.save(medicalRecord);
        ((DoctorServiceImpl) AopContext.currentProxy()).saveDiagnose(diagnoses, medicalRecord.getId(),false,false);
        schedule.setHaveRegistrationAmount(schedule.getHaveRegistrationAmount()+1);
        scheduleService.update(schedule);
        return CommonUtil.successJson();
    }

    /**
     * 存为病历模板
     */
    @Override
    @Transactional
    public JSONObject saveMRTemplate(MedicalRecord record,Integer doctorID,String name,Integer level) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        MedicalRecordTemplate template=copyMedicalRecord(record);
        template=setImportantInfo(template, doctorID, name, level);
        return ((DoctorServiceImpl) AopContext.currentProxy()).saveRecordAndDiagnoseAsTemp(record, template, doctorID);
    }

    private MedicalRecordTemplate setImportantInfo(MedicalRecordTemplate template,Integer doctorID,String  name,Integer level){
        template.setId(null);
        template.setCreatedById(doctorID);
        template.setName(name);
        template.setDepartmentId(getDeptNo(doctorID));
        template.setLevelId(level);
        return template;
    }

    @Override
    @Transactional
    public JSONObject updateMedicalRecordTem(MedicalRecordTemplate record, Integer doctorID) {
        Integer id=record.getId();
        if(id==null || medicalRecordTemplateService.findById(id)==null)
            return CommonUtil.errorJson(ErrorEnum.E_707.addErrorParamName("medicalRecordTemplate"));
        medicalRecordTemplateService.update(record);
        return CommonUtil.successJson();
    }


    @Override
    @Transactional
    public JSONObject deleteMedicalRecordTemp(Integer id,Integer doctorId){
        MedicalRecordTemplate template=medicalRecordTemplateService.findById(id);
        if(template==null)
            return CommonUtil.errorJson(ErrorEnum.E_707.addErrorParamName("medicalRecordTemplate"));
        if(!template.getCreatedById().equals(doctorId)){
            return CommonUtil.errorJson(ErrorEnum.E_706.addErrorParamName("medicalRecordTemplate"));
        }
            medicalRecordTemplateService.deleteById(id);
        return CommonUtil.successJson();
    }


    @Override
    public  JSONObject openInspection(Integer registrationId){
        Registration registration=registrationService.findById(registrationId);
        if(!(registration.getState().equals(Constants.FIRST_DIAGNOSIS) || registration.getState().equals(Constants.SUSPECT)))
            return CommonUtil.errorJson(ErrorEnum.E_506.addErrorParamName("notFirstDiagnose"));
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getInspectionResult(Integer id) {
        return  CommonUtil.successJson(resultService.getInspectionResult(id));
    }

    @Override
    public JSONObject saveFinalDiagnose(Integer registrationId,Integer medicalRecordId,List<Integer> diagnoses) throws Exception {
        Registration registration = registrationService.findById(registrationId);
        if(registration==null)
            return  CommonUtil.errorJson(ErrorEnum.E_608.addErrorParamName("registration"));
        MedicalRecord record =medicalRecordService.findById(medicalRecordId);

        if(record==null || !record.getRegistrationId().equals(registrationId))
            return  CommonUtil.errorJson(ErrorEnum.E_608.addErrorParamName("medicalRecordId"));
        if(record.getFirstDiagnose()!=null && !record.getFirstDiagnose().isEmpty())
            return  CommonUtil.errorJson(ErrorEnum.E_615.addErrorParamName("finalDiagnose"));


        registration.setState(Constants.FINAL_DIAGNOSIS);
        registrationService.update(registration);
        ((DoctorServiceImpl) AopContext.currentProxy()).saveDiagnose(diagnoses, medicalRecordId,true,false);
        return CommonUtil.successJson();
    }


    /**
     * 提交申请检查..项目
     * @param object
     * @return
     * @throws Exception
     */
    /**
     * 保存处置
     * @param object
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JSONObject saveInspection(JSONObject object,Boolean isDisposal,Integer doctorId)throws  Exception{

        Integer registrationId=Integer.parseInt(object.get("registrationId").toString());
        MedicalRecord record=medicalRecordService.getByRegistrationId(registrationId);
        Integer medicalRecordId=record.getId();
        //是否有初诊
        if (record.getFirstDiagnose()==null  || record.getFirstDiagnose().size()==0) {
            return  CommonUtil.errorJson(ErrorEnum.E_616.addErrorParamName("firstDiagnose"));
        }
        //是否是确诊处置
        if(isDisposal){
            //是否 是 有检查项目且没有确诊的情况
            if(inspectionApplicationService.hasMedicalRecordInspectionNotDone(medicalRecordId)){
                if(record.getFinalDiagnose()==null)
                    return CommonUtil.errorJson(ErrorEnum.E_616.addErrorParamName("finalDiagnose"));
            }
        }
        //是否能找到 挂号
        Registration registration=registrationService.findById(registrationId);
        if(registration==null)
            return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName("registrationId"));
        if(!isDisposal)
            registration.setState(Constants.SUSPECT);

        //挂号更新
        registrationService.update(registration);

        //是否是模板数据

        InspectionTemplate template= JSONObject.parseObject(JSONObject.toJSONString(object.get("template")), InspectionTemplate.class);
        List<InspectionApplication> applicationList=template.getApplications();
        //保存模板非药项目
        if (applicationList!=null){
            for (InspectionApplication r : applicationList){
                if(r.getNonDrugId()==null || nonDrugService.findById(r.getNonDrugId())==null ){
                    return CommonUtil.errorJson(ErrorEnum.E_701.addErrorParamName(r.getNonDrugId().toString()));
                }
                InspectionApplication application=new InspectionApplication(medicalRecordId,r.getNonDrugId(),new Date(),false,r.getEmerged(),r.getQuantity(),false,false);
                inspectionApplicationService.save(application);
                Payment payment = setInspectionPayment(r,registration.getPatientId());
                paymentService.save(payment);
            }
        }

        //保存模板非药项目
        String check="";
        List<Prescription> prescriptionList=template.getPrescriptions();
        if(prescriptionList!=null){
            for(Prescription prescription:prescriptionList){
                check=checkPrescription(prescription);
                if(!check.equals(""))
                    return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(check));
                Prescription p2=new Prescription(prescription);
                p2.setTemplate(false);
                p2.setItemId(medicalRecordId);
                prescriptionService.save(p2);
                Payment p=setPrescriptionPayment(prescription, registration.getPatientId());
            }
        }
        return CommonUtil.successJson();
    }


    private  Payment setInspectionPayment(InspectionApplication application,Integer patientId){
        Payment payment = new Payment();
        NonDrug nonDrug=nonDrugService.findById(application.getNonDrugId());
        payment.setQuantity(application.getQuantity());
        payment.setUnitPrice(nonDrug.getPrice());
        payment.setCreateTime(new Date(System.currentTimeMillis()));
        payment.setPatientId(patientId);
        payment.setPaymentTypeId(nonDrug.getFeeTypeId());
        payment.setItemId(application.getId());
        payment.setState(Constants.PRODUCE_PAYMENT);
        return payment;
    }

    private  Payment setPrescriptionPayment(Prescription application,Integer patientId){
        Payment payment = new Payment();
        Drug drug=drugService.findById(application.getDrugId());
        payment.setQuantity(application.getAmount());
        payment.setUnitPrice(drug.getPrice());
        payment.setCreateTime(new Date(System.currentTimeMillis()));
        payment.setPatientId(patientId);
        if(drug.getDrugType().equals(Constants.WESTEN_DRUG))
            payment.setPaymentTypeId(Constants.WESTERN_DRUG_FEE_TYPE);
        else if(drug.getDrugType().equals(Constants.TRADITIONAL_DRUG))
            payment.setPaymentTypeId(Constants.CHI_DRUG_FEE_TYPE);
        else
            payment.setPaymentTypeId(Constants.MEDIUM_DRUG_FEE_TYPE);
        payment.setItemId(application.getId());
        payment.setState(Constants.PRODUCE_PAYMENT);
        return payment;
    }

    /**
     * 新建检查检验/检验/处置模板
     * @param template
     * @param doctorId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public JSONObject saveInspectionAsTemplate(InspectionTemplate template,Integer doctorId) throws Exception {

        List<InspectionApplication> applicationList=template.getApplications();
        List<Prescription> prescriptionList=template.getPrescriptions();

        template.setCreatedById(doctorId);
        template.setDepartmentId(doctorService.getDeptNo(doctorId));
        if (template.getDepartmentId()==null)
            return CommonUtil.errorJson(ErrorEnum.E_610);
        inspectionTemplateService.save(template);
        Integer tempId=template.getId();
        //保存模板非药项目
        if (applicationList!=null){
            for (InspectionApplication r : applicationList){
                if(r.getNonDrugId()==null || nonDrugService.findById(r.getNonDrugId())==null ){
                    return CommonUtil.errorJson(ErrorEnum.E_701.addErrorParamName(r.getNonDrugId().toString()));
                }
                InspectionApplication application=new InspectionApplication(tempId,r.getNonDrugId(),new Date(),false,r.getEmerged(),r.getQuantity(),false,true);
                inspectionApplicationService.save(application);
            }
        }
        //保存模板非药项目
        String check="";
        if(prescriptionList!=null){
            for(Prescription prescription:prescriptionList){
                check=checkPrescription(prescription);
                if(!check.equals(""))
                    return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(check));
                Prescription p2=new Prescription(prescription);
                p2.setTemplate(true);
                p2.setItemId(tempId);
                prescriptionService.save(p2);
            }
        }
        return CommonUtil.successJson();
    }

    @Override
    @Transactional
    public JSONObject updateInspectionTem(InspectionTemplate template ,Integer doctorId) throws Exception{
        Integer tempId=template.getId();
        if(tempId==null || inspectionTemplateService.findById(tempId)==null)
            return  CommonUtil.errorJson(ErrorEnum.E_707.addErrorParamName("inspectionTemplate"));
        inspectionTemplateService.deleteRelationship(tempId);
        List<Prescription> prescriptions=template.getPrescriptions();
        if(prescriptions!=null){
            for (Prescription p:prescriptions){
                p.setItemId(tempId);
                p.setTemplate(true);
                prescriptionService.save(p);
            }
        }
        List<InspectionApplication> applications=template.getApplications();
        if(applications!=null){
            for (InspectionApplication p:applications){
                p.setItemId(tempId);
                p.setTemplate(true);
                p.setCreateTime(new Date(System.currentTimeMillis()));
                inspectionApplicationService.save(p);
            }
        }
        return  CommonUtil.successJson();
    }

    @Override
    public JSONObject getInspectionTemByName(String name) {
        return CommonUtil.successJson(inspectionTemplateService.getInspectionTemByName(name));
    }

    @Override
    @Transactional
    public JSONObject deleteInspectionTemp(Integer id,Integer doctorId) {
        InspectionTemplate template=inspectionTemplateService.findById(id);
        if(template==null)
            return CommonUtil.errorJson(ErrorEnum.E_707.addErrorParamName("inspectionTemplate"));
        if(!template.getCreatedById().equals(doctorId)){
            return CommonUtil.errorJson(ErrorEnum.E_706.addErrorParamName("inspectionTemplate"));
        }
        inspectionTemplateService.deleteRelationship(id);
        inspectionApplicationService.deleteById(id);
        return CommonUtil.successJson();
    }


    @Override
    @Transactional
    public JSONObject savePrescriptions(List<Prescription> prescriptions,Integer medicalRecordId,Integer registationId) throws Exception {

        Registration registration=registrationService.findById(registationId);

        if(!registration.getState().equals(Constants.FINAL_DIAGNOSIS))
            return CommonUtil.errorJson(ErrorEnum.E_703);
        String check;
        for (Prescription p:prescriptions){
            p.setCreateTime(new Date(System.currentTimeMillis()));
            p.setId(null);
            p.setItemId(medicalRecordId);
            p.setTemplate(false);
            check=checkPrescription(p);
            if(!check.equals("")){
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(check));
            }
            prescriptionService.save(p);
            Payment payment = setPrescriptionPayment(p, registration.getPatientId());
        }
//        registration.setState(Constants.FINISH_DIAGNOSIS);
        registrationService.update(registration);
        return CommonUtil.successJson();
    }

    @Override
    @Transactional
    public JSONObject savePrescriptionsTemp(DrugTemplate template,Integer medicalRecordId,Integer doctorId) throws Exception{
        MedicalRecord medicalRecord=medicalRecordService.findById(medicalRecordId);
        template.setHerbal(!medicalRecord.getWesternMedicine());
        template.setCreatedById(doctorId);
        template.setDepartmentId(doctorService.getDeptNo(doctorId));
        drugTemplateService.save(template);

        for (Prescription p: template.getPrescriptions()){
            p.setId(null);
            p.setTemplate(true);
            p.setItemId(template.getId());
            p.setCreateTime(new Date(System.currentTimeMillis()));
            String check=checkPrescription(p);
            if(!check.equals("")){
                return CommonUtil.errorJson(ErrorEnum.E_501.addErrorParamName(check));
            }
            prescriptionService.save(p);
        }
        return CommonUtil.successJson();
    }

    @Override
    public JSONObject getPrescriptionsTemByName(String name) {
        return CommonUtil.successJson(drugTemplateService.getPrescriptionsTemByName(name));
    }


    private String checkPrescription(Prescription p){
        if(p.getUsageId()==null){
            return "usageId";
        }
        if(p.getFrequency()==null || p.getFrequency().equals("")){
            return "frequency";
        }
        if(p.getDrugId()==null || drugService.findById(p.getDrugId())==null)
            return "drug";
        if(p.getAmount()==null )
            return "amount";
        if(p.getUseAmount()==null || p.getUseAmount().equals(""))
            return "useAmount";
        if(p.getDays()==null)
            return "days";
        if(p.getNeedSkinTest()==null)
            return "needSkinTest";
        return "";
    }

    @Transactional
    public JSONObject saveRecordAndDiagnoseAsTemp(MedicalRecord record, MedicalRecordTemplate template, Integer doctorID) throws RuntimeException {
        try{
            medicalRecordTemplateService.save(template);
        }catch (Exception e){
            throw new  RuntimeException("medicalRecoredTemplate");
        }
        try {
            List<Diagnose> firstD=record.getFirstDiagnose();
            ((DoctorServiceImpl) AopContext.currentProxy()).saveDiagnoseTemp(firstD, template.getId());
            List<Diagnose> finalD=record.getFinalDiagnose();
            ((DoctorServiceImpl) AopContext.currentProxy()).saveDiagnoseTemp(finalD, template.getId() );
        } catch (Exception e) {
            throw new  RuntimeException("diagnose");
        }
        return CommonUtil.successJson();
    }

    private String cheakMedicalRecord(MedicalRecord record){
        if (registrationService.findById(record.getRegistrationId())==null)
            return "RegistrationId";
        if (record.getSelfDescription()==null || record.getSelfDescription().equals("")  )
            return "SelfDescription";
        if (record.getCurrentSymptom()==null || record.getCurrentSymptom().equals(""))
            return "CurrentSymptom";
        if (record.getIsWesternMedicine()==null )
            return  "isWesternMedicne";
        return "";
    }

    private String cheakInspection(InspectionApplication inspectionApplication){
        if (medicalRecordService.findById(inspectionApplication.getItemId())==null)
            return "MedicalRecord";
        if (nonDrugService.findById(inspectionApplication.getNonDrugId())==null)
            return "NonDrug";
        if (inspectionApplication.getQuantity()==null || inspectionApplication.getQuantity().equals("")  )
            return "Quantity";
        if (inspectionApplication.getCreateTime()==null || inspectionApplication.getCreateTime().equals("")  )
            return "CreateTime";
        return "";
    }

    private MedicalRecordTemplate copyMedicalRecord(MedicalRecord record) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        MedicalRecordTemplate template = JSON.parseObject(JSON.toJSONString(record), MedicalRecordTemplate.class);
        return template;
    }


    @Transactional
    public   void saveDiagnose(List<Integer> diagnoses ,Integer itemId,Boolean isMajor,Boolean isTemplate) throws  Exception{
        if(diagnoses!=null){
            for (Integer integer:diagnoses){
                Diagnose diagnose=new Diagnose();
                diagnose.setId(null);
                diagnose.setItemId(itemId);
                diagnose.setIsMajor(isMajor);
                diagnose.setCreateTime(new Date(System.currentTimeMillis()));
                diagnose.setTemplate(isTemplate);
                diagnose.setDiseaseId(integer);
                diagnoseService.save(diagnose);
            }
        }
    }

    @Transactional
    public void saveDiagnoseTemp(List<Diagnose> diagnoses,Integer templateId){
        if(diagnoses!=null){
            for (Diagnose d:diagnoses){
                Diagnose diagnose=new Diagnose();
                diagnose.setId(null);
                diagnose.setItemId(templateId);
                diagnose.setIsMajor(d.getIsMajor());
                diagnose.setCreateTime(d.getCreateTime());
                diagnose.setTemplate(true);
                diagnose.setDiseaseId(d.getDiseaseId());
                diagnoseService.save(diagnose);
            }
        }
    }


    @Transactional
    public  JSONObject finishDiagnose(Integer registrationId){
        Registration registration=registrationService.findById(registrationId);
        if(registration==null)
            return CommonUtil.errorJson(ErrorEnum.E_705);
        if(!registration.getState().equals(Constants.FIRST_DIAGNOSIS) && !registration.getState().equals(Constants.FINAL_DIAGNOSIS))
            return CommonUtil.errorJson(ErrorEnum.E_703);
        registration.setState(Constants.FINISH_DIAGNOSIS);
        registrationService.update(registration);
        return CommonUtil.successJson();
    }
}
