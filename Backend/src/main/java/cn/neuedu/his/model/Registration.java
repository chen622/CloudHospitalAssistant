package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 是否需要病历本
     */
    @Column(name = "need_book")
    private Boolean needBook;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "doctor_id")
    private Integer doctorId;

    /**
     * 状态，参考常量表
     */
    private Integer state;

    @Column(name = "schedule_id")
    private Integer scheduleId;

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
     * 获取是否需要病历本
     *
     * @return need_book - 是否需要病历本
     */
    public Boolean getNeedBook() {
        return needBook;
    }

    /**
     * 设置是否需要病历本
     *
     * @param needBook 是否需要病历本
     */
    public void setNeedBook(Boolean needBook) {
        this.needBook = needBook;
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
     * 获取状态，参考常量表
     *
     * @return state - 状态，参考常量表
     */
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态，参考常量表
     *
     * @param state 状态，参考常量表
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return schedule_id
     */
    public Integer getScheduleId() {
        return scheduleId;
    }

    /**
     * @param scheduleId
     */
    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }
}