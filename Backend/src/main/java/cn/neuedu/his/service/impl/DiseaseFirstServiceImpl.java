package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DiseaseFirstMapper;
import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.service.DiseaseFirstService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DiseaseFirstServiceImpl extends AbstractService<DiseaseFirst> implements DiseaseFirstService {

    @Autowired
    private DiseaseFirstMapper diseaseFirstMapper;

    @Override
    public DiseaseFirst getDiseaseByname(String name) {
        return diseaseFirstMapper.getDiseaseByname(name);
    }
}
