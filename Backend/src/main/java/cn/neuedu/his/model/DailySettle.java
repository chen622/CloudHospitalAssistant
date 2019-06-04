package cn.neuedu.his.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "daily_settle")
public class DailySettle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "make_date")
    private Date makeDate;

    /**
     * 收费员id
     */
    @Column(name = "admin_id")
    private Integer adminId;

    /**
     * 制表人id
     */
    @Column(name = "make_id")
    private Integer makeId;

    /**
     * 总金额
     */
    @Column(name = "total_fee")
    private BigDecimal totalFee;

    /**
     * 药费总额
     */
    @Column(name = "drug_fee")
    private BigDecimal drugFee;

    /**
     * 检查项目费
     */
    @Column(name = "inspection_fee")
    private BigDecimal inspectionFee;

    /**
     * 挂号费
     */
    @Column(name = "registration_fee")
    private BigDecimal registrationFee;

    /**
     * 其他费用
     */
    @Column(name = "other_fee")
    private Long otherFee;

    /**
     * 一般发票数量
     */
    @Column(name = "normal_invoice_amount")
    private Integer normalInvoiceAmount;

    @Column(name = "anew_invoice_amount")
    private Integer anewInvoiceAmount;

    @Column(name = "again_invoice_amount")
    private Integer againInvoiceAmount;

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
     * @return start_date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return end_date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return make_date
     */
    public Date getMakeDate() {
        return makeDate;
    }

    /**
     * @param makeDate
     */
    public void setMakeDate(Date makeDate) {
        this.makeDate = makeDate;
    }

    /**
     * 获取收费员id
     *
     * @return admin_id - 收费员id
     */
    public Integer getAdminId() {
        return adminId;
    }

    /**
     * 设置收费员id
     *
     * @param adminId 收费员id
     */
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /**
     * 获取制表人id
     *
     * @return make_id - 制表人id
     */
    public Integer getMakeId() {
        return makeId;
    }

    /**
     * 设置制表人id
     *
     * @param makeId 制表人id
     */
    public void setMakeId(Integer makeId) {
        this.makeId = makeId;
    }

    /**
     * 获取总金额
     *
     * @return total_fee - 总金额
     */
    public BigDecimal getTotalFee() {
        return totalFee;
    }

    /**
     * 设置总金额
     *
     * @param totalFee 总金额
     */
    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    /**
     * 获取药费总额
     *
     * @return drug_fee - 药费总额
     */
    public BigDecimal getDrugFee() {
        return drugFee;
    }

    /**
     * 设置药费总额
     *
     * @param drugFee 药费总额
     */
    public void setDrugFee(BigDecimal drugFee) {
        this.drugFee = drugFee;
    }

    /**
     * 获取检查项目费
     *
     * @return inspection_fee - 检查项目费
     */
    public BigDecimal getInspectionFee() {
        return inspectionFee;
    }

    /**
     * 设置检查项目费
     *
     * @param inspectionFee 检查项目费
     */
    public void setInspectionFee(BigDecimal inspectionFee) {
        this.inspectionFee = inspectionFee;
    }

    /**
     * 获取挂号费
     *
     * @return registration_fee - 挂号费
     */
    public BigDecimal getRegistrationFee() {
        return registrationFee;
    }

    /**
     * 设置挂号费
     *
     * @param registrationFee 挂号费
     */
    public void setRegistrationFee(BigDecimal registrationFee) {
        this.registrationFee = registrationFee;
    }

    /**
     * 获取其他费用
     *
     * @return other_fee - 其他费用
     */
    public Long getOtherFee() {
        return otherFee;
    }

    /**
     * 设置其他费用
     *
     * @param otherFee 其他费用
     */
    public void setOtherFee(Long otherFee) {
        this.otherFee = otherFee;
    }

    /**
     * 获取一般发票数量
     *
     * @return normal_invoice_amount - 一般发票数量
     */
    public Integer getNormalInvoiceAmount() {
        return normalInvoiceAmount;
    }

    /**
     * 设置一般发票数量
     *
     * @param normalInvoiceAmount 一般发票数量
     */
    public void setNormalInvoiceAmount(Integer normalInvoiceAmount) {
        this.normalInvoiceAmount = normalInvoiceAmount;
    }

    /**
     * @return anew_invoice_amount
     */
    public Integer getAnewInvoiceAmount() {
        return anewInvoiceAmount;
    }

    /**
     * @param anewInvoiceAmount
     */
    public void setAnewInvoiceAmount(Integer anewInvoiceAmount) {
        this.anewInvoiceAmount = anewInvoiceAmount;
    }

    /**
     * @return again_invoice_amount
     */
    public Integer getAgainInvoiceAmount() {
        return againInvoiceAmount;
    }

    /**
     * @param againInvoiceAmount
     */
    public void setAgainInvoiceAmount(Integer againInvoiceAmount) {
        this.againInvoiceAmount = againInvoiceAmount;
    }
}