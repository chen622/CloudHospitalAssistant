package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "create_time")
    private Date createTime = new Date(System.currentTimeMillis());

    /**
     * 缴费员
     */
    @Column(name = "operator_id")
    private Integer operatorId;

    @Column(name = "patient_id")
    private Integer patientId;

    /**
     * 参考常量表
     * 结费类型：自费/医保等
     */
    @Column(name = "settlement_type_id")
    private Integer settlementTypeId;

    /**
     * 费用类型：西药费/挂号费之类的
     */
    @Column(name = "payment_type_id")
    private Integer paymentTypeId;

    @Column(name = "item_id")
    private Integer itemId;

    private Integer state;

    @Column(name = "invoice_id")
    private Integer invoiceId;

    @Column(name = "project_operator_id")
    private Integer projectOperatorId;

    /**
     * 对账的时候冻结账单
     */
    @Column(name = "is_frozen")
    private Boolean isFrozen;

    @Column(name = "doctor_id")
    private Integer doctorId;

    private Prescription prescription;
    private InspectionApplication application;
    private Patient patient;
    private PaymentType paymentType;
    private User user;
    private ConstantVariable stateVariable;


    public ConstantVariable getStateVariable() {
        return stateVariable;
    }

    public void setStateVariable(ConstantVariable stateVariable) {
        this.stateVariable = stateVariable;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public InspectionApplication getApplication() {
        return application;
    }

    public void setApplication(InspectionApplication application) {
        this.application = application;
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
     * @return unit_price
     */
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    /**
     * @param unitPrice
     */
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
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
     * 获取对账的时候冻结账单
     *
     * @return is_frozen - 对账的时候冻结账单
     */
    public Boolean getIsFrozen() {
        return isFrozen;
    }

    /**
     * 设置对账的时候冻结账单
     *
     * @param isFrozen 对账的时候冻结账单
     */
    public void setIsFrozen(Boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    /**
     * 获取缴费员
     *
     * @return operator_id - 缴费员
     */
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * 设置缴费员
     *
     * @param operatorId 缴费员
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return patient_id
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * @param patientId
     */
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    /**
     * 获取参考常量表
     * 结费类型：自费/医保等
     *
     * @return settlement_type_id - 参考常量表
     * 结费类型：自费/医保等
     */
    public Integer getSettlementTypeId() {
        return settlementTypeId;
    }

    /**
     * 设置参考常量表
     * 结费类型：自费/医保等
     *
     * @param settlementTypeId 参考常量表
     *                         结费类型：自费/医保等
     */
    public void setSettlementTypeId(Integer settlementTypeId) {
        this.settlementTypeId = settlementTypeId;
    }

    /**
     * 获取费用类型：西药费/挂号费之类的
     *
     * @return payment_type_id - 费用类型：西药费/挂号费之类的
     */
    public Integer getPaymentTypeId() {
        return paymentTypeId;
    }

    /**
     * 设置费用类型：西药费/挂号费之类的
     *
     * @param paymentTypeId 费用类型：西药费/挂号费之类的
     */
    public void setPaymentTypeId(Integer paymentTypeId) {
        this.paymentTypeId = paymentTypeId;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Boolean getFrozen() {
        return isFrozen;
    }

    public void setFrozen(Boolean frozen) {
        isFrozen = frozen;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Integer getProjectOperatorId() {
        return projectOperatorId;
    }

    public void setProjectOperatorId(Integer projectOperatorId) {
        this.projectOperatorId = projectOperatorId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }
}