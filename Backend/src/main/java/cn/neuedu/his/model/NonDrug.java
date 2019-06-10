package cn.neuedu.his.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "non_drug")
public class NonDrug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "standard")
    private String standard;

    @Column(name = "code")
    private String code;

    @Column(name = "executive_department")
    private Integer executiveDepartment;

    @Column(name = "is_delete")
    private Boolean isDelete;

    private Department department;

    public Boolean getDelete() {
        return isDelete;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getExecutiveDepartment() {
        return executiveDepartment;
    }

    public void setExecutiveDepartment(Integer excutiveDepartment) {
        this.executiveDepartment = executiveDepartment;
    }

    /**
     * 非药物费用类别
     */
    @Column(name = "fee_type_id")
    private Integer feeTypeId;

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return standard
     */
    public String getStandard() {
        return standard;
    }

    /**
     * @param standard
     */
    public void setStandard(String standard) {
        this.standard = standard;
    }

    /**
     * 获取非药物费用类别
     *
     * @return fee_type_id - 非药物费用类别
     */
    public Integer getFeeTypeId() {
        return feeTypeId;
    }

    /**
     * 设置非药物费用类别
     *
     * @param feeTypeId 非药物费用类别
     */
    public void setFeeTypeId(Integer feeTypeId) {
        this.feeTypeId = feeTypeId;
    }

}