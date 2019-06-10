package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "drug_template_relationship")
public class DrugTemplateRelationship {
    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "item_id")
    private Integer itemId;

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

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}