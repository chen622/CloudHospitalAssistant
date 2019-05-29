package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DiseaseSecondMapper;
import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.service.DiseaseSecondService;
import cn.neuedu.his.util.inter.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@Service
public class DiseaseSecondServiceImpl extends AbstractService<DiseaseSecond> implements DiseaseSecondService {

    @Autowired
    private DiseaseSecondMapper diseaseSecondMapper;

    @Override
    public List<DiseaseSecond> findByName(String name) {
        return diseaseSecondMapper.findByName(name);
    }

    @Override
    public List<DiseaseSecond> getAll() {
        return diseaseSecondMapper.getAll();
    }
}
