package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "price_amount")
    private BigDecimal priceAmount;

    @Column(name = "created_date")
    private Date createdDate = new Date(System.currentTimeMillis());

    @Column(name = "anew_amount")
    private Integer anewAmount;

    @Column(name = "again_amount")
    private Integer againAmount;

    @Column(name = "daily_settle_id")
    private Integer dailySettleId;

    @Column(name = "operator_id")
    private Integer operatorId;

    private ArrayList<Payment> paymentList;

    private Payment payment;

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
     * @return price_amount
     */
    public BigDecimal getPriceAmount() {
        return priceAmount;
    }

    /**
     * @param priceAmount
     */
    public void setPriceAmount(BigDecimal priceAmount) {
        this.priceAmount = priceAmount;
    }

    /**
     * @return created_date
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * @param createdDate
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public ArrayList<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(ArrayList<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public Integer getAnewAmount() {
        return anewAmount;
    }

    public void setAnewAmount(Integer anewAmount) {
        this.anewAmount = anewAmount;
    }

    public Integer getAgainAmount() {
        return againAmount;
    }

    public void setAgainAmount(Integer againAmount) {
        this.againAmount = againAmount;
    }

    public Integer getDailySettleId() {
        return dailySettleId;
    }

    public void setDailySettleId(Integer dailySettleId) {
        this.dailySettleId = dailySettleId;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}