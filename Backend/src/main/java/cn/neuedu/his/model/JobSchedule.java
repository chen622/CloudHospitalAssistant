package cn.neuedu.his.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "job_schedule")
public class JobSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "current_registration_amount")
    private Integer currentRegistrationAmount;

    @Column(name = "registration_type_id")
    private Integer registrationTypeId;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "limit_registration_amount")
    private Integer limitRegistrationAmount;

    /**
     * 午别，参照常量表
     */
    private Integer period;

    private Date date;

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
     * @return doctor_id
     */
    public Integer getDoctorId() {
        return doctorId;
    }

    /**
     * @param doctorId
     */
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * @return current_registration_amount
     */
    public Integer getCurrentRegistrationAmount() {
        return currentRegistrationAmount;
    }

    /**
     * @param currentRegistrationAmount
     */
    public void setCurrentRegistrationAmount(Integer currentRegistrationAmount) {
        this.currentRegistrationAmount = currentRegistrationAmount;
    }

    /**
     * @return registration_type_id
     */
    public Integer getRegistrationTypeId() {
        return registrationTypeId;
    }

    /**
     * @param registrationTypeId
     */
    public void setRegistrationTypeId(Integer registrationTypeId) {
        this.registrationTypeId = registrationTypeId;
    }

    /**
     * @return is_valid
     */
    public Boolean getIsValid() {
        return isValid;
    }

    /**
     * @param isValid
     */
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * @return limit_registration_amount
     */
    public Integer getLimitRegistrationAmount() {
        return limitRegistrationAmount;
    }

    /**
     * @param limitRegistrationAmount
     */
    public void setLimitRegistrationAmount(Integer limitRegistrationAmount) {
        this.limitRegistrationAmount = limitRegistrationAmount;
    }

    /**
     * 获取午别，参照常量表
     *
     * @return period - 午别，参照常量表
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * 设置午别，参照常量表
     *
     * @param period 午别，参照常量表
     */
    public void setPeriod(Integer period) {
        this.period = period;
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
}