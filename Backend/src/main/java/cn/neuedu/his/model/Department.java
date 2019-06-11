package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

public class Department implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private DepartmentKind departmentKind;

    @Column(name = "kind_id")
    private Integer kindId;

    @Column(name = "is_delete")
    private Boolean isDelete;

    public Boolean isDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    private String code;

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
     * @return kind_id
     */
    public Integer getKindId() {
        return kindId;
    }

    /**
     * @param kindId
     */
    public void setKindId(Integer kindId) {
        this.kindId = kindId;
    }

    /**
     * @return code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    public DepartmentKind getDepartmentKind() {
        return departmentKind;
    }

    public void setDepartmentKind(DepartmentKind departmentKind) {
        this.departmentKind = departmentKind;
    }
}