package cn.neuedu.his.service;
import cn.neuedu.his.model.InspectionTemplateRelationship;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InspectionTemplateRelationshipService extends Service<InspectionTemplateRelationship> {
    public List<InspectionTemplateRelationship> getItem(Integer templateID, Integer nonDrugType);
}
