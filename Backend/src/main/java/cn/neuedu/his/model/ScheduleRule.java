package cn.neuedu.his.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "schedule_rule")
public class ScheduleRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer period;

    @Column(name = "registration_quantity")
    private Integer registrationQuantity;

    @Column(name = "operator_id")
    private Integer operatorId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    @Column(name = "registration_type_id")
    private Integer registrationTypeId;

    private Integer day;

    private ConstantVariable periodVariable;

    private RegistrationType registrationType;

    private User user;

    private Doctor doctor;

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
     * @return period
     */
    public Integer getPeriod() {
        return period;
    }

    /**
     * @param period
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }

    /**
     * @return registration_quantity
     */
    public Integer getRegistrationQuantity() {
        return registrationQuantity;
    }

    /**
     * @param registrationQuantity
     */
    public void setRegistrationQuantity(Integer registrationQuantity) {
        this.registrationQuantity = registrationQuantity;
    }

    /**
     * @return operator_id
     */
    public Integer getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
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

    public ConstantVariable getPeriodVariable() {
        return periodVariable;
    }

    public void setPeriodVariable(ConstantVariable periodVariable) {
        this.periodVariable = periodVariable;
    }
}