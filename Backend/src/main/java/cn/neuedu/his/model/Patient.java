package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 身份证号
     */
    @Column(name = "identity_id")
    private String identityId;

    private String password;

    private Boolean sex;

    @Column(name = "real_name")
    private String realName;

    private String username;

    @Column(name = "last_login_time")
    private Date lastLoginTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "last_password_modify_time")
    private Date lastPasswordModifyTime;

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
}