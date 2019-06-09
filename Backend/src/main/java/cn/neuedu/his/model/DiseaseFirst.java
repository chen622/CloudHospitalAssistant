package cn.neuedu.his.model;

import javax.persistence.*;

@Table(name = "disease_first")
public class DiseaseFirst {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "is_delete")
    private Boolean isDelete;

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Boolean getDelete() {
        return isDelete;
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
}