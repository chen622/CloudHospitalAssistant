package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DrugTemplateRelationshipMapper;
import cn.neuedu.his.model.DrugTemplateRelationship;
import cn.neuedu.his.service.DrugTemplateRelationshipService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DrugTemplateRelationshipServiceImpl extends AbstractService<DrugTemplateRelationship> implements DrugTemplateRelationshipService {

    @Autowired
    private DrugTemplateRelationshipMapper drugTemplateRelationshipMapper;

}
