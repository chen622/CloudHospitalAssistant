package cn.neuedu.his.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "payment_type")
public class PaymentType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private String name;

    /**
     * 大类：0 -- 挂号费
1 -- 处方
2 -- 项目
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
     * 获取大类：0 -- 挂号费
1 -- 处方
2 -- 项目
     *
     * @return type - 大类：0 -- 挂号费
1 -- 处方
2 -- 项目
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置大类：0 -- 挂号费
1 -- 处方
2 -- 项目
     *
     * @param type 大类：0 -- 挂号费
1 -- 处方
2 -- 项目
     */
    public void setType(Integer type) {
        this.type = type;
    }
}