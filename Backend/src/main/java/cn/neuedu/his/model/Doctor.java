package cn.neuedu.his.model;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Map;

public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 职称,参照常量表
     */
    @Column(name = "title_id")
    private Integer titleId;



    /**
     * 是否排班
     */
    @Column(name = "can_arrange")
    private Boolean canArrange;


    @Column(name = "is_delete")
    private Boolean isDelete;


    @Transient
    private String titleName;

    @Transient
    private String realName;

    private JSONObject feeMap;

    public JSONObject getFeeMap() {
        return feeMap;
    }

    public void setFeeMap(JSONObject feeMap) {
        this.feeMap = feeMap;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
     * 获取职称,参照常量表
     *
     * @return title_id - 职称,参照常量表
     */
    public Integer getTitleId() {
        return titleId;
    }

    /**
     * 设置职称,参照常量表
     *
     * @param titleId 职称,参照常量表
     */
    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    /**
     * 获取是否排班
     *
     * @return can_arrange - 是否排班
     */
    public Boolean getCanArrange() {
        return canArrange;
    }

    /**
     * 设置是否排班
     *
     * @param canArrange 是否排班
     */
    public void setCanArrange(Boolean canArrange) {
        this.canArrange = canArrange;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }
}