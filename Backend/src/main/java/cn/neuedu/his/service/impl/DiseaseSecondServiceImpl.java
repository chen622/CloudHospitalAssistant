package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DiseaseSecondMapper;
import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.service.DiseaseSecondService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
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

    @Override
    public DiseaseSecond findByIcdId(String icdId) {
        return diseaseSecondMapper.findByIcdId(icdId);
    }

    @Override
    public DiseaseSecond findByDiseaseCoding(String diseaseCoding) {
        return diseaseSecondMapper.findByDiseaseCoding(diseaseCoding);
    }

    @Override
    public void insertDiseaseSecond(DiseaseSecond diseaseSecond) {
        //判断国际编码是否重复
        if (this.findByIcdId(diseaseSecond.getIcdId()) != null)
            throw new RuntimeException("621");
            //return CommonUtil.errorJson(ErrorEnum.E_621);

        //判断疾病编码是否重复
        if (this.findByDiseaseCoding(diseaseSecond.getDiseaseCoding()) != null)
            throw new RuntimeException("622");
        //return CommonUtil.errorJson(ErrorEnum.E_622);

        //判断疾病名称是否重复
        if (this.findByName(diseaseSecond.getName()) != null)
            throw new RuntimeException("623");
        //return CommonUtil.errorJson(ErrorEnum.E_623);

        //判断疾病类别是否存在
        if (this.findById(diseaseSecond.getDiseaseFirstId()) != null)
            throw new RuntimeException("624");
        //return CommonUtil.errorJson(ErrorEnum.E_624);

        this.save(diseaseSecond);
    }

    @Override
    public void delateDiseaseSecond(Integer id) {
        DiseaseSecond diseaseSecond = this.findById(id);
        //判断疾病是否存在
        if (diseaseSecond == null)
            throw new RuntimeException("625");

        diseaseSecond.setDelete(true);
        this.update(diseaseSecond);
    }

    @Override
    public List<DiseaseSecond> selectDiseaseSecond(String name) {
        List<DiseaseSecond> diseaseSeconds = this.findByName(name);

        return diseaseSeconds;
    }

    @Override
    public void modifyDiseaseSecond(DiseaseSecond diseaseSecond) {
        //判断国际编码是否存在
        if (this.findByIcdId(diseaseSecond.getIcdId()) != null)
            throw new RuntimeException("621");

        //判断疾病编码是否存在
        if (this.findByDiseaseCoding(diseaseSecond.getDiseaseCoding()) != null)
            throw new RuntimeException("622");

        //判断疾病类别是否存在
        if (this.findById(diseaseSecond.getDiseaseFirstId()) == null)
            throw new RuntimeException("624");

        this.update(diseaseSecond);
    }
}
