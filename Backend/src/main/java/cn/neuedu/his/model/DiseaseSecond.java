package cn.neuedu.his.model;

import javax.persistence.*;

@Table(name = "disease_second")
public class DiseaseSecond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "icd_id")
    private String icdId;

    @Column(name = "disease_coding")
    private String diseaseCoding;

    private String name;

    @Column(name = "disease_first_id")
    private Integer diseaseFirstId;

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
     * @return icd_id
     */
    public String getIcdId() {
        return icdId;
    }

    /**
     * @param icdId
     */
    public void setIcdId(String icdId) {
        this.icdId = icdId;
    }

    /**
     * @return disease_coding
     */
    public String getDiseaseCoding() {
        return diseaseCoding;
    }

    /**
     * @param diseaseCoding
     */
    public void setDiseaseCoding(String diseaseCoding) {
        this.diseaseCoding = diseaseCoding;
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
     * @return disease_first_id
     */
    public Integer getDiseaseFirstId() {
        return diseaseFirstId;
    }

    /**
     * @param diseaseFirstId
     */
    public void setDiseaseFirstId(Integer diseaseFirstId) {
        this.diseaseFirstId = diseaseFirstId;
    }
}