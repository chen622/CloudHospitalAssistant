package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "drug_template_relationship")
public class DrugTemplateRelationship {
    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "drug_id")
    private Integer drugId;

    /**
     * @return template_id
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * @param templateId
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * @return drug_id
     */
    public Integer getDrugId() {
        return drugId;
    }

    /**
     * @param drugId
     */
    public void setDrugId(Integer drugId) {
        this.drugId = drugId;
    }
}