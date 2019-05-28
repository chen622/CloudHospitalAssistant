package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

public class Drug {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String formulation;

    private String factory;

    /**
     * 包装单位
     */
    @Column(name = "package_company")
    private String packageCompany;

    private String spell;

    /**
     * 规格
     */
    private String standard;

    private BigDecimal price;

    private String name;

    private String code;
    private Integer stockAmount;
    private Integer drugType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(Integer stockAmount) {
        this.stockAmount = stockAmount;
    }

    public Integer getDrugType() {
        return drugType;
    }

    public void setDrugType(Integer drugType) {
        this.drugType = drugType;
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
     * @return formulation
     */
    public String getFormulation() {
        return formulation;
    }

    /**
     * @param formulation
     */
    public void setFormulation(String formulation) {
        this.formulation = formulation;
    }

    /**
     * @return factory
     */
    public String getFactory() {
        return factory;
    }

    /**
     * @param factory
     */
    public void setFactory(String factory) {
        this.factory = factory;
    }

    /**
     * 获取包装单位
     *
     * @return package_company - 包装单位
     */
    public String getPackageCompany() {
        return packageCompany;
    }

    /**
     * 设置包装单位
     *
     * @param packageCompany 包装单位
     */
    public void setPackageCompany(String packageCompany) {
        this.packageCompany = packageCompany;
    }

    /**
     * @return spell
     */
    public String getSpell() {
        return spell;
    }

    /**
     * @param spell
     */
    public void setSpell(String spell) {
        this.spell = spell;
    }

    /**
     * 获取规格
     *
     * @return standard - 规格
     */
    public String getStandard() {
        return standard;
    }

    /**
     * 设置规格
     *
     * @param standard 规格
     */
    public void setStandard(String standard) {
        this.standard = standard;
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
}