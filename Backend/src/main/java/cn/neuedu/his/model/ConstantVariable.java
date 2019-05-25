package cn.neuedu.his.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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