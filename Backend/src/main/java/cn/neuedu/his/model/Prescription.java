package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class Prescription implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 用法，参照常量表
     */
    @Column(name = "usage_id")
    private Integer usageId;

    @Column(name = "item_id")
    private Integer itemId;

    /**
     * 频率
     */
    @Column( name =  "frequency")
    private String frequency;

    @Column(name = "drug_id")
    private Integer drugId;

    @Column (name = "amount")
    private Integer amount;

    @Column (name = "days")
    private Integer days;

    /**
     * 单次用量
     */
    @Column(name = "use_amount")
    private String useAmount;

    @Column(name = "note")
    private String note;

    @Column(name = "need_skin_test")
    private Boolean needSkinTest;

    @Column(name = "skin_test_result")
    private Boolean skinTestResult;

    @Column (name = "is_template")
    private Boolean isTemplate;

    @Column(name = "create_time")
    private Date createTime = new Date(System.currentTimeMillis());
    private Integer feeTypeId;

    private Drug drug;
    private Payment payment;

    public Prescription() {
        return;
    }

    public Prescription(Prescription p,Integer feeTypeId,Integer medicalRecord,Boolean isTemplate) {
        this.usageId = p.usageId;
        this.itemId = medicalRecord;
        this.frequency = p.frequency;
        this.drugId = p.drugId;
        this.amount = p.amount;
        this.days = p.days;
        this.useAmount = p.useAmount;
        this.note = p.note;
        this.needSkinTest = p.needSkinTest;
        this.skinTestResult = p.skinTestResult;
        this.isTemplate = isTemplate;
        createTime=new Date(System.currentTimeMillis());
        this.feeTypeId=feeTypeId;
    }


    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
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
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
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

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }
}