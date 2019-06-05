package cn.neuedu.his.model;

import javax.persistence.*;

@Table(name = "department_kind")
public class DepartmentKind {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "kind_name")
    private String kindName;

    /**
     * 参照常量表
     */
    @Column(name = "classification_id")
    private Integer classificationId;

    private ConstantVariable constantVariable;

    @Column(name = "is_delete")
    private Boolean isDelete;

    public Boolean isDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
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
     * @return kind_name
     */
    public String getKindName() {
        return kindName;
    }

    /**
     * @param kindName
     */
    public void setKindName(String kindName) {
        this.kindName = kindName;
    }

    /**
     * 获取参照常量表
     *
     * @return classification_id - 参照常量表
     */
    public Integer getClassificationId() {
        return classificationId;
    }

    /**
     * 设置参照常量表
     *
     * @param classificationId 参照常量表
     */
    public void setClassificationId(Integer classificationId) {
        this.classificationId = classificationId;
    }

    public ConstantVariable getConstantVariable() {
        return constantVariable;
    }

    public void setConstantVariable(ConstantVariable constantVariable) {
        this.constantVariable = constantVariable;
    }
}