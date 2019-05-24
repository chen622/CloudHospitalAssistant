package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Diagnose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "medical_record_id")
    private Integer medicalRecordId;

    @Column(name = "disease_id")
    private Integer diseaseId;

    private Date date;

    /**
     * 是否为确诊
     */
    @Column(name = "is_major")
    private Boolean isMajor;

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
     * @return medical_record_id
     */
    public Integer getMedicalRecordId() {
        return medicalRecordId;
    }

    /**
     * @param medicalRecordId
     */
    public void setMedicalRecordId(Integer medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    /**
     * @return disease_id
     */
    public Integer getDiseaseId() {
        return diseaseId;
    }

    /**
     * @param diseaseId
     */
    public void setDiseaseId(Integer diseaseId) {
        this.diseaseId = diseaseId;
    }

    /**
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * 获取是否为确诊
     *
     * @return is_major - 是否为确诊
     */
    public Boolean getIsMajor() {
        return isMajor;
    }

    /**
     * 设置是否为确诊
     *
     * @param isMajor 是否为确诊
     */
    public void setIsMajor(Boolean isMajor) {
        this.isMajor = isMajor;
    }
}