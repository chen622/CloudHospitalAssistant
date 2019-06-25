package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.model.InspectionTemplate;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface InspectionTemplateMapper extends MyMapper<InspectionTemplate> {
    List<InspectionTemplate> getWhichICanUse(Integer doctorId, Integer departmentId);

    public List<InspectionTemplate> getHospitalCheckTemps(Integer doctorID, Integer level);

    public List<InspectionTemplate> getDeptCheckTemps(Integer doctorID, Integer level);

    public List<InspectionTemplate> getPersonalCheckTemps(Integer doctorID, Integer level);

    public void deleteRelationship(Integer id);

    List<InspectionTemplate> getInspectionTemByName(String name);
}