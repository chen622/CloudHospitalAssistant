package cn.neuedu.his.model;

import javax.persistence.*;
import java.util.List;

@Table(name = "inspection_template")
public class InspectionTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "department_id")
    private Integer departmentId;

    @Column(name = "level")
    private Integer level;

    @Column(name = "name")
    private String name;

    @Column(name = "created_by_id")
    private Integer createdById;

    private ConstantVariable lev;

    private List<Prescription> prescriptions;
    private List<InspectionApplication> applications;

    public ConstantVariable getLev() {
        return lev;
    }

    public void setLev(ConstantVariable lev) {
        this.lev = lev;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<InspectionApplication> getApplications() {
        return applications;
    }

    public void setApplications(List<InspectionApplication> applications) {
        this.applications = applications;
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
     * @return department_id
     */
    public Integer getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId
     */
    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level
     */
    public void setLevel(Integer level) {
        this.level = level;
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
     * @return created_by_id
     */
    public Integer getCreatedById() {
        return createdById;
    }

    /**
     * @param createdById
     */
    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }
}