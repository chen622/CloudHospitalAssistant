package cn.neuedu.his.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.*;
import java.util.List;

@Table(name = "constant_variable")
public class ConstantVariable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    /**
     * 0: 本表类别
     */
    private Integer type;

    @Column(name = "is_delete")
    private Boolean isDelete;

    private List<Drug> drugs;

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }


    public Boolean getDelete() {
        return isDelete;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
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
     * 获取0: 本表类别
     *
     * @return type - 0: 本表类别
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置0: 本表类别
     *
     * @param type 0: 本表类别
     */
    public void setType(Integer type) {
        this.type = type;
    }
}