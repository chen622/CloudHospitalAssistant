package cn.neuedu.his.model;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name = "inspection_template_relationship")
public class InspectionTemplateRelationship {
    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "item_id")
    private Integer itemId;

    /**
     * 0是药品
1是非药品
     */
    @Column(name = "item_type")
    private Boolean itemType;

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
     * @return item_id
     */
    public Integer getItemId() {
        return itemId;
    }

    /**
     * @param itemId
     */
    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    /**
     * 获取0是药品
1是非药品
     *
     * @return item_type - 0是药品
1是非药品
     */
    public Boolean getItemType() {
        return itemType;
    }

    /**
     * 设置0是药品
1是非药品
     *
     * @param itemType 0是药品
1是非药品
     */
    public void setItemType(Boolean itemType) {
        this.itemType = itemType;
    }
}