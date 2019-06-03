package cn.neuedu.his.service;
import cn.neuedu.his.model.Drug;
import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.model.InspectionTemplate;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InspectionTemplateService extends Service<InspectionTemplate> {
    public List<InspectionTemplate> getHospitalCheckTemps(Integer doctorID,Integer level);
    public List<InspectionTemplate> getDeptCheckTemps(Integer doctorID,Integer level);
    public List<InspectionTemplate> getPersonalCheckTemps(Integer doctorID,Integer level);
    public void deleteRelationship(Integer id);
    List<InspectionTemplate> getInspectionTemByName(String name);
}
