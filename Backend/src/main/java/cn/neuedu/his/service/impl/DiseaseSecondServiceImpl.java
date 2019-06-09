package cn.neuedu.his.service.impl;

import cn.neuedu.his.mapper.DiseaseSecondMapper;
import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.service.DiseaseFirstService;
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
    @Autowired
    private DiseaseFirstService diseaseFirstService;

    /**
     * 模糊查询
     * @param name
     * @return
     */
    @Override
    public List<DiseaseSecond> findByName(String name) {
        return diseaseSecondMapper.findByName(name);
    }

    @Override
    public List<DiseaseSecond> getAll() {
        return diseaseSecondMapper.getAll();
    }

    /**
     * 模糊查询ICDid
     * @param icdId
     * @return
     */
    @Override
    public List<DiseaseSecond> findByIcdId(String icdId) {
        return diseaseSecondMapper.selectDiseaseByIcd(icdId);
    }

    /**
     * 准确查询疾病代码
     * @param diseaseCoding
     * @return
     */
    @Override
    public DiseaseSecond findByDiseaseCoding(String diseaseCoding) {
        return diseaseSecondMapper.findByDiseaseCoding(diseaseCoding);
    }

    /**
     * 插入疾病
     * @param diseaseSecond
     * @throws RuntimeException
     */
    @Override
    public void insertDiseaseSecond(DiseaseSecond diseaseSecond) throws RuntimeException{
        //判断国际编码是否重复
        DiseaseSecond diseaseSecond1 = diseaseSecondMapper.findByIcdId(diseaseSecond.getIcdId());
        if (diseaseSecond1 != null && diseaseSecond1.getId() != diseaseSecond.getId())
            throw new RuntimeException("621");
            //return CommonUtil.errorJson(ErrorEnum.E_621);

        //判断疾病编码是否重复
        diseaseSecond1 = diseaseSecondMapper.findByDiseaseCoding(diseaseSecond.getDiseaseCoding());
        if (diseaseSecond1 != null && diseaseSecond1.getId() != diseaseSecond.getId())
            throw new RuntimeException("622");
        //return CommonUtil.errorJson(ErrorEnum.E_622);

        //判断疾病名称是否重复
        diseaseSecond1 = diseaseSecondMapper.selectByName(diseaseSecond.getName());
        if (diseaseSecond1 != null && diseaseSecond1.getId()!=diseaseSecond.getId())
            throw new RuntimeException("623");
        //return CommonUtil.errorJson(ErrorEnum.E_623);

        //判断疾病类别是否存在
        if (diseaseFirstService.findById(diseaseSecond.getDiseaseFirstId()) != null)
            throw new RuntimeException("624");
        //return CommonUtil.errorJson(ErrorEnum.E_624);

        this.save(diseaseSecond);
    }

    /**
     * 删除疾病
     * @param id
     */
    @Override
    public void delateDiseaseSecond(Integer id) {
        DiseaseSecond diseaseSecond = this.findById(id);
        //判断疾病是否存在
        if (diseaseSecond == null)
            throw new RuntimeException("625");

        diseaseSecond.setDelete(true);
        this.update(diseaseSecond);
    }


    /**
     * 修改疾病
     * @param diseaseSecond
     */
    @Override
    public void modifyDiseaseSecond(DiseaseSecond diseaseSecond) {
        DiseaseSecond diseaseSecond1 = diseaseSecondMapper.findByIcdId(diseaseSecond.getIcdId());
        if (diseaseSecond1 != null && diseaseSecond1.getId() != diseaseSecond.getId())
            throw new RuntimeException("621");
        //return CommonUtil.errorJson(ErrorEnum.E_621);

        //判断疾病编码是否重复
        diseaseSecond1 = diseaseSecondMapper.findByDiseaseCoding(diseaseSecond.getDiseaseCoding());
        if (diseaseSecond1 != null && diseaseSecond1.getId() != diseaseSecond.getId())
            throw new RuntimeException("622");
        //return CommonUtil.errorJson(ErrorEnum.E_622);

        //判断疾病名称是否重复
        diseaseSecond1 = diseaseSecondMapper.selectByName(diseaseSecond.getName());
        if (diseaseSecond1 != null && diseaseSecond1.getId()!=diseaseSecond.getId())
            throw new RuntimeException("623");
        //return CommonUtil.errorJson(ErrorEnum.E_623);

        //判断疾病类别是否存在
        if (diseaseFirstService.findById(diseaseSecond.getDiseaseFirstId()) != null)
            throw new RuntimeException("624");
        //return CommonUtil.errorJson(ErrorEnum.E_624);

        this.update(diseaseSecond);
    }

    @Override
    public List<DiseaseSecond> findByDiseaseFirstid(Boolean authenication, Integer type) {
        return diseaseSecondMapper.findByDiseaseFirstid(authenication,type);
    }
}
