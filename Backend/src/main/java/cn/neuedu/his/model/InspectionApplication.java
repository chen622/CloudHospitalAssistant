package cn.neuedu.his.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "inspection_application")
public class InspectionApplication implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "non_drug_id")
    private Integer nonDrugId;

    @Column(name = "create_time")
    private Date createTime = new Date(System.currentTimeMillis());

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(name = "is_emerged")
    private Boolean isEmerged;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_canceled")
    private Boolean isCanceled;

    @Column(name = "is_template")
    private Boolean isTemplate;

    private Integer feeTypeId;

    private Boolean isCheck;

    private Payment payment;

    private List<Payment> payments;
    private Department department;
    private NonDrug nonDrug;

    private Patient patient;
    private User user;

    private List<InspectionResult> results = new ArrayList<>();

    public Patient getPatient() {
        return patient;
    }

    public List<InspectionResult> getResults() {
        return results;
    }

    public void setResults(List<InspectionResult> results) {
        this.results = results;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Boolean getCheck() {
        return isCheck;
    }

    public void setCheck(Boolean check) {
        isCheck = check;
    }

    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

    public InspectionApplication() {
        return;
    }

    public InspectionApplication(Integer itemId, Integer nonDrugId, Date createTime, Boolean isDone, Boolean isEmerged, Integer quantity, Boolean isCanceled, Boolean isTemplate, Integer feeTypeId) {
        this.itemId = itemId;
        this.nonDrugId = nonDrugId;
        this.createTime = createTime;
        this.isDone = isDone;
        this.isEmerged = isEmerged;
        this.quantity = quantity;
        this.isCanceled = isCanceled;
        this.isTemplate = isTemplate;
        this.feeTypeId = feeTypeId;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Boolean getEmerged() {
        return isEmerged;
    }

    public void setEmerged(Boolean emerged) {
        isEmerged = emerged;
    }


    public Boolean getTemplate() {
        return isTemplate;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public NonDrug getNonDrug() {
        return nonDrug;
    }

    public void setNonDrug(NonDrug nonDrug) {
        this.nonDrug = nonDrug;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
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
     * @return item_id
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
     * @return non_drug_id
     */
    public Integer getNonDrugId() {
        return nonDrugId;
    }

    /**
     * @param nonDrugId
     */
    public void setNonDrugId(Integer nonDrugId) {
        this.nonDrugId = nonDrugId;
    }

    /**
     * @return create_time
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
     * @return quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return is_canceled
     */
    public Boolean getIsCanceled() {
        return isCanceled;
    }

    /**
     * @param isCanceled
     */
    public void setIsCanceled(Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
}