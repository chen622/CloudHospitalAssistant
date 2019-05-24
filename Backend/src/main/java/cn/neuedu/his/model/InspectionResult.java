package cn.neuedu.his.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "inspection_result")
public class InspectionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "operator_id")
    private Integer operatorId;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "inspection_application_id")
    private Integer inspectionApplicationId;

    private String text;

    private String picture;

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
     * @return department_id
     */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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
     * @return inspection_application_id
     */
    public Integer getInspectionApplicationId() {
        return inspectionApplicationId;
    }

    /**
     * @param inspectionApplicationId
     */
    public void setInspectionApplicationId(Integer inspectionApplicationId) {
        this.inspectionApplicationId = inspectionApplicationId;
    }

    /**
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return picture
     */
    public String getPicture() {
        return picture;
    }

    /**
     * @param picture
     */
    public void setPicture(String picture) {
        this.picture = picture;
    }
}