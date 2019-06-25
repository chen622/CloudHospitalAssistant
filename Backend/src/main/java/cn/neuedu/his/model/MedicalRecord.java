package cn.neuedu.his.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Table(name = "medical_record")
public class MedicalRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "self_description")
    private String selfDescription;

    @Column(name = "is_pregnant")
    private Boolean isPregnant;

    @Column(name = "current_symptom")
    private String currentSymptom;

    @Column(name = "history_symptom")
    private String historySymptom;

    private List<Prescription> prescriptions = new ArrayList<>();
    private List<InspectionApplication> inspectionApplications = new ArrayList<>();



    /**
     * 是否为西医诊断
     */
    @Column(name = "is_western_medicine")
    private Boolean isWesternMedicine;

    @Column(name = "allergy_history")
    private String allergyHistory;

    @Column(name = "body_examination")
    private String bodyExamination;

    @Column(name = "registration_id")
    private Integer registrationId;

    @Column(name = "previous_treatment")
    private String previousTreatment;

    @Column(name = "notification")
    private String notification;

    @Column(name = "check_advice")
    private String checkAdvice;

    private List<Diagnose> firstDiagnose;

    private List<Diagnose> finalDiagnose;

    public Boolean getPregnant() {
        return isPregnant;
    }

    public void setPregnant(Boolean pregnant) {
        isPregnant = pregnant;
    }

    public Boolean getWesternMedicine() {
        return isWesternMedicine;
    }

    public void setWesternMedicine(Boolean westernMedicine) {
        isWesternMedicine = westernMedicine;
    }

    public List<Diagnose> getFirstDiagnose() {
        return firstDiagnose;
    }

    public void setFirstDiagnose(List<Diagnose> firstDiagnose) {
        this.firstDiagnose = firstDiagnose;
    }

    public List<Diagnose> getFinalDiagnose() {
        return finalDiagnose;
    }

    public void setFinalDiagnose(List<Diagnose> finalDiagnose) {
        this.finalDiagnose = finalDiagnose;
    }

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return self_description
     */
    public String getSelfDescription() {
        return selfDescription;
    }

    /**
     * @param selfDescription
     */
    public void setSelfDescription(String selfDescription) {
        this.selfDescription = selfDescription;
    }

    /**
     * @return is_pregnant
     */
    public Boolean getIsPregnant() {
        return isPregnant;
    }

    /**
     * @param isPregnant
     */
    public void setIsPregnant(Boolean isPregnant) {
        this.isPregnant = isPregnant;
    }

    /**
     * @return current_symptom
     */
    public String getCurrentSymptom() {
        return currentSymptom;
    }

    /**
     * @param currentSymptom
     */
    public void setCurrentSymptom(String currentSymptom) {
        this.currentSymptom = currentSymptom;
    }

    /**
     * @return history_symptom
     */
    public String getHistorySymptom() {
        return historySymptom;
    }

    /**
     * @param historySymptom
     */
    public void setHistorySymptom(String historySymptom) {
        this.historySymptom = historySymptom;
    }

    /**
     * 获取是否为西医诊断
     *
     * @return is_western_medicine - 是否为西医诊断
     */
    public Boolean getIsWesternMedicine() {
        return isWesternMedicine;
    }

    /**
     * 设置是否为西医诊断
     *
     * @param isWesternMedicine 是否为西医诊断
     */
    public void setIsWesternMedicine(Boolean isWesternMedicine) {
        this.isWesternMedicine = isWesternMedicine;
    }

    /**
     * @return allergy_history
     */
    public String getAllergyHistory() {
        return allergyHistory;
    }

    /**
     * @param allergyHistory
     */
    public void setAllergyHistory(String allergyHistory) {
        this.allergyHistory = allergyHistory;
    }

    /**
     * @return body_examination
     */
    public String getBodyExamination() {
        return bodyExamination;
    }

    /**
     * @param bodyExamination
     */
    public void setBodyExamination(String bodyExamination) {
        this.bodyExamination = bodyExamination;
    }

    /**
     * @return registration_id
     */
    public Integer getRegistrationId() {
        return registrationId;
    }

    /**
     * @param registrationId
     */
    public void setRegistrationId(Integer registrationId) {
        this.registrationId = registrationId;
    }

    /**
     * @return previous_treatment
     */
    public String getPreviousTreatment() {
        return previousTreatment;
    }

    /**
     * @param previousTreatment
     */
    public void setPreviousTreatment(String previousTreatment) {
        this.previousTreatment = previousTreatment;
    }

    /**
     *
     * @return prescriptions
     */
    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    /**
     *
     * @param prescriptions
     */
    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    /**
     *
     * @return List<InspectionApplication>
     */
    public List<InspectionApplication> getInspectionApplications() {
        return inspectionApplications;
    }

    /**
     *
     * @param inspectionApplications
     */
    public void setInspectionApplications(List<InspectionApplication> inspectionApplications) {
        this.inspectionApplications = inspectionApplications;
    }
}