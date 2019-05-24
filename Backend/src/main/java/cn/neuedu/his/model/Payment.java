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
    private Date createTime;

    @Column(name = "have_completed")
    private Boolean haveCompleted;

    /**
     * 是否为退费
     */
    @Column(name = "is_retreat")
    private Boolean isRetreat;

    /**
     * 对账的时候冻结账单
     */
    @Column(name = "is_frozen")
    private Boolean isFrozen;

    /**
     * 缴费员
     */
    @Column(name = "operator_id")
    private Integer operatorId;

    @Column(name = "patient_id")
    private Integer patientId;

    /**
     * 参考常量表
结费类型：自费/医保等
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
     * @return have_completed
     */
    public Boolean getHaveCompleted() {
        return haveCompleted;
    }

    /**
     * @param haveCompleted
     */
    public void setHaveCompleted(Boolean haveCompleted) {
        this.haveCompleted = haveCompleted;
    }

    /**
     * 获取是否为退费
     *
     * @return is_retreat - 是否为退费
     */
    public Boolean getIsRetreat() {
        return isRetreat;
    }

    /**
     * 设置是否为退费
     *
     * @param isRetreat 是否为退费
     */
    public void setIsRetreat(Boolean isRetreat) {
        this.isRetreat = isRetreat;
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
结费类型：自费/医保等
     *
     * @return settlement_type_id - 参考常量表
结费类型：自费/医保等
     */
    public Integer getSettlementTypeId() {
        return settlementTypeId;
    }

    /**
     * 设置参考常量表
结费类型：自费/医保等
     *
     * @param settlementTypeId 参考常量表
结费类型：自费/医保等
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
}