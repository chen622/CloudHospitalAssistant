package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用法，参照常量表
     */
    @Column(name = "usage_id")
    private Integer usageId;

    @Column(name = "medical_record_id")
    private Integer medicalRecordId;

    /**
     * 频率
     */
    private String frequency;

    @Column(name = "drug_id")
    private Integer drugId;

    private Integer amount;

    private Integer days;

    /**
     * 单次用量
     */
    @Column(name = "use_amount")
    private String useAmount;

    private String note;

    @Column(name = "need_skin_test")
    private Boolean needSkinTest;

    @Column(name = "skin_test_result")
    private Boolean skinTestResult;

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
     * 获取用法，参照常量表
     *
     * @return usage_id - 用法，参照常量表
     */
    public Integer getUsageId() {
        return usageId;
    }

    /**
     * 设置用法，参照常量表
     *
     * @param usageId 用法，参照常量表
     */
    public void setUsageId(Integer usageId) {
        this.usageId = usageId;
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
     * 获取频率
     *
     * @return frequency - 频率
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * 设置频率
     *
     * @param frequency 频率
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    /**
     * @return drug_id
     */
    public Integer getDrugId() {
        return drugId;
    }

    /**
     * @param drugId
     */
    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }

    /**
     * @return amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * @param amount
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * @return days
     */
    public Integer getDays() {
        return days;
    }

    /**
     * @param days
     */
    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * 获取单次用量
     *
     * @return use_amount - 单次用量
     */
    public String getUseAmount() {
        return useAmount;
    }

    /**
     * 设置单次用量
     *
     * @param useAmount 单次用量
     */
    public void setUseAmount(String useAmount) {
        this.useAmount = useAmount;
    }

    /**
     * @return note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return need_skin_test
     */
    public Boolean getNeedSkinTest() {
        return needSkinTest;
    }

    /**
     * @param needSkinTest
     */
    public void setNeedSkinTest(Boolean needSkinTest) {
        this.needSkinTest = needSkinTest;
    }

    /**
     * @return skin_test_result
     */
    public Boolean getSkinTestResult() {
        return skinTestResult;
    }

    /**
     * @param skinTestResult
     */
    public void setSkinTestResult(Boolean skinTestResult) {
        this.skinTestResult = skinTestResult;
    }
}