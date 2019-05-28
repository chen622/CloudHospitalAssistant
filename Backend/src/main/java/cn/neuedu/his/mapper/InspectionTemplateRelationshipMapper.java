package cn.neuedu.his.mapper;

import cn.neuedu.his.model.InspectionTemplateRelationship;
import cn.neuedu.his.util.inter.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface InspectionTemplateRelationshipMapper extends MyMapper<InspectionTemplateRelationship> {
    public List<InspectionTemplateRelationship> getItem(@Param("templateID") Integer templateID,@Param("nonDrugType") Integer nonDrugType);
}