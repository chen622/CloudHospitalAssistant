package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DiseaseFirstMapper;
import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.service.DiseaseFirstService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
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

    @Override
    public void insertDiseaseFirst(DiseaseFirst diseaseFirst) {
        //判断类别名称是否重复
        if (this.getDiseaseByname(diseaseFirst.getName()) != null)
            throw new RuntimeException("620");

        this.save(diseaseFirst);
    }

    @Override
    public void modifyDiseaseFirst(DiseaseFirst diseaseFirst) {

        //判断疾病是否存在
        if (this.getDiseaseByname(diseaseFirst.getName()) != null)
            throw new RuntimeException("620");

        this.update(diseaseFirst);
    }

    @Override
    public void deleteDiseaseFirst(Integer id) {

        DiseaseFirst diseaseFirst = this.findById(id);
        //判断id是否存在（疾病是否存在）
        if (diseaseFirst == null)
            throw new RuntimeException("624");

        this.update(diseaseFirst);
    }

    @Override
    public DiseaseFirst selectDiseaseFirst(String name) {
        DiseaseFirst diseaseFirst = this.getDiseaseByname(name);

        //判断疾病是否存在
        if (diseaseFirst == null)
            throw new RuntimeException("620");

        return diseaseFirst;
    }
}
