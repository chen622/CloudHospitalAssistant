package cn.neuedu.his.model;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Column(name = "registrar_id")
    private Integer registrarId;

    @Column(name = "create_time")
    private Date createTime = new Date(System.currentTimeMillis());

    private Integer sequence;

    @Column(name = "serial_number")
    private Integer serialNumber;

    private Integer age;

    private Patient patient;

    @Transient
    private Float medicalFee;
    @Transient
    private Float inspectionFee;
    @Transient
    private Float theOtherFee;
    @Transient
    private Float registrationFee;
    @Transient
    private Float diagnosisFee;

    //状态名字
    @Transient
    private String stateName;

    //挂号时段
    @Transient
    private String period;
    @Transient
    private String doctorName;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    private RegistrationType registrationType;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Float getMedicalFee() {
        return medicalFee;
    }

    public void setMedicalFee(Float medicalFee) {
        this.medicalFee = medicalFee;
    }

    public Float getInspectionFee() {
        return inspectionFee;
    }

    public void setInspectionFee(Float inspectionFee) {
        this.inspectionFee = inspectionFee;
    }

    public Float getTheOtherFee() {
        return theOtherFee;
    }

    public void setTheOtherFee(Float theOtherFee) {
        this.theOtherFee = theOtherFee;
    }

    public Float getRegistrationFee() {
        return registrationFee;
    }

    public void setRegistrationFee(Float registrationFee) {
        this.registrationFee = registrationFee;
    }

    public Float getDiagnosisFee() {
        return diagnosisFee;
    }

    public void setDiagnosisFee(Float diagnosisFee) {
        this.diagnosisFee = diagnosisFee;
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

    public Integer getRegistrarId() {
        return registrarId;
    }

    public void setRegistrarId(Integer registrarId) {
        this.registrarId = registrarId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public RegistrationType getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(RegistrationType registrationType) {
        this.registrationType = registrationType;
    }

    public String setSerialNumber() {
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        String dateStr = format.format(createTime);
        String dataStr = String.valueOf(id % 100000);
        return dateStr + dataStr;
    }
}