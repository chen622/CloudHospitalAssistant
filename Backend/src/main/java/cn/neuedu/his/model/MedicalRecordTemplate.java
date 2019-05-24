package cn.neuedu.his.model;

import javax.persistence.*;

@Table(name = "medical_record_template")
public class MedicalRecordTemplate {
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

    @Column(name = "is_western_medicine")
    private Boolean isWesternMedicine;

    @Column(name = "allergy_history")
    private String allergyHistory;

    @Column(name = "body_examination")
    private String bodyExamination;

    @Column(name = "created_by_id")
    private Integer createdById;

    @Column(name = "department_id")
    private Integer departmentId;

    /**
     * 权限级别，参照常量表
     */
    @Column(name = "level_id")
    private Integer levelId;

    @Column(name = "previous_treatment")
    private String previousTreatment;

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
     * @return is_western_medicine
     */
    public Boolean getIsWesternMedicine() {
        return isWesternMedicine;
    }

    /**
     * @param isWesternMedicine
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
     * @return created_by_id
     */
    public Integer getCreatedById() {
        return createdById;
    }

    /**
     * @param createdById
     */
    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    /**
     * @return department_id
     */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取权限级别，参照常量表
     *
     * @return level_id - 权限级别，参照常量表
     */
    public Integer getLevelId() {
        return levelId;
    }

    /**
     * 设置权限级别，参照常量表
     *
     * @param levelId 权限级别，参照常量表
     */
    public void setLevelId(Integer levelId) {
        this.levelId = levelId;
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
}