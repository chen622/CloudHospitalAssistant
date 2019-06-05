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

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "disease_id")
    private Integer diseaseId;

    @Column(name = "create_time")
    private Date createTime = new Date(System.currentTimeMillis());

    @Column(name = "is_template")
    private Boolean isTemplate;

    private DiseaseSecond diseaseSecond;


    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
    }

    public Boolean getMajor() {
        return isMajor;
    }

    public void setMajor(Boolean major) {
        isMajor = major;
    }

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
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param medicalRecordId
     */
    public void setItemId(Integer medicalRecordId) {
        this.itemId = medicalRecordId;
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
     * @return createTime
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
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

    public DiseaseSecond getDiseaseSecond() {
        return diseaseSecond;
    }

    public void setDiseaseSecond(DiseaseSecond diseaseSecond) {
        this.diseaseSecond = diseaseSecond;
    }
}