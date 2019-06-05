package cn.neuedu.his.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "registration_type")
public class RegistrationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "is_default")
    private Boolean isDefault;

    private BigDecimal price;

    @Column(name = "display_sequence_number")
    private Integer displaySequenceNumber;

    @Column(name = "is_delete")
    private boolean isDelete;

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
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
     * @return is_default
     */
    public Boolean getIsDefault() {
        return isDefault;
    }

    /**
     * @param isDefault
     */
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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
     * @return display_sequence_number
     */
    public Integer getDisplaySequenceNumber() {
        return displaySequenceNumber;
    }

    /**
     * @param displaySequenceNumber
     */
    public void setDisplaySequenceNumber(Integer displaySequenceNumber) {
        this.displaySequenceNumber = displaySequenceNumber;
    }
}