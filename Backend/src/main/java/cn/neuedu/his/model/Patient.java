package cn.neuedu.his.model;

import com.alibaba.fastjson.JSONArray;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 身份证号
     */
    @Column(name = "identity_id")
    private String identityId;

    @Column(name = "password")
    private String password;

    @Column(name = "sex")
    private Boolean sex;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "username")
    private String username;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "create_time")
    private Date createTime = new Date(System.currentTimeMillis());

    @Column(name = "last_password_modify_time")
    private Date lastPasswordModifyTime;

    @Column(name = "open_id")
    private String openId;

    @Column(name = "session_key")
    private String sessionKey;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column
    private Boolean isConfirm;

    @Transient
    private Integer age;

    @Transient
    private Registration registration;

    private JSONArray registrations;


    public void updateWechatData(String openId, String sessionKey) {
        setSessionKey(sessionKey);
        setOpenId(openId);
    }

    public Boolean getConfirm() {
        return isConfirm;
    }

    public void setConfirm(Boolean confirm) {
        isConfirm = confirm;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public void addRegistrations(Registration registration) {
        registrations.add(registration);
    }

    public JSONArray getRegistrations() {
        return registrations;
    }

    public void setRegistrations(JSONArray registrations) {
        this.registrations = registrations;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    private List<Payment> paymentList;

    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
     * 获取身份证号
     *
     * @return identity_id - 身份证号
     */
    public String getIdentityId() {
        return identityId;
    }

    /**
     * 设置身份证号
     *
     * @param identityId 身份证号
     */
    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return sex
     */
    public Boolean getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    /**
     * @return real_name
     */
    public String getRealName() {
        return realName;
    }

    /**
     * @param realName
     */
    public void setRealName(String realName) {
        this.realName = realName;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return last_login_time
     */
    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * @param lastLoginTime
     */
    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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
     * @return last_password_modify_time
     */
    public Date getLastPasswordModifyTime() {
        return lastPasswordModifyTime;
    }

    /**
     * @param lastPasswordModifyTime
     */
    public void setLastPasswordModifyTime(Date lastPasswordModifyTime) {
        this.lastPasswordModifyTime = lastPasswordModifyTime;
    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }
}