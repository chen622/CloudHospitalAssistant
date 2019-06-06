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

    @Column(name = "create_time")
    private Date createTime = new Date(System.currentTimeMillis());

    private Date date;

    private User user;
    private Doctor doctor;
    private RegistrationType registrationType;
    private ConstantVariable constantVariable;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public ConstantVariable getConstantVariable() {
        return constantVariable;
    }

    public void setConstantVariable(ConstantVariable constantVariable) {
        this.constantVariable = constantVariable;
    }
}