package cn.neuedu.his.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "inspection_application")
public class InspectionApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "item_id")
    private Integer itemId;

    @Column(name = "non_drug_id")
    private Integer nonDrugId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "is_done")
    private Boolean isDone;

    @Column(name = "is_emerged")
    private  Boolean isEmerged;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_canceled")
    private Boolean isCanceled;

    @Column (name = "is_template")
    private Boolean isTemplate;

    public InspectionApplication() {
        return;
    }

    public InspectionApplication(Integer itemId, Integer nonDrugId, Date createTime, Boolean isDone, Boolean isEmerged, Integer quantity, Boolean isCanceled, Boolean isTemplate) {
        this.itemId = itemId;
        this.nonDrugId = nonDrugId;
        this.createTime = createTime;
        this.isDone = isDone;
        this.isEmerged = isEmerged;
        this.quantity = quantity;
        this.isCanceled = isCanceled;
        this.isTemplate = isTemplate;
    }

    public Boolean getDone() {
        return isDone;
    }

    public void setDone(Boolean done) {
        isDone = done;
    }

    public Boolean getEmerged() {
        return isEmerged;
    }

    public void setEmerged(Boolean emerged) {
        isEmerged = emerged;
    }


    public Boolean getTemplate() {
        return isTemplate;
    }

    public void setTemplate(Boolean template) {
        isTemplate = template;
    }

    public Boolean getCanceled() {
        return isCanceled;
    }

    public void setCanceled(Boolean canceled) {
        isCanceled = canceled;
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
     * @return non_drug_id
     */
    public Integer getNonDrugId() {
        return nonDrugId;
    }

    /**
     * @param nonDrugId
     */
    public void setNonDrugId(Integer nonDrugId) {
        this.nonDrugId = nonDrugId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return is_canceled
     */
    public Boolean getIsCanceled() {
        return isCanceled;
    }

    /**
     * @param isCanceled
     */
    public void setIsCanceled(Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }
}