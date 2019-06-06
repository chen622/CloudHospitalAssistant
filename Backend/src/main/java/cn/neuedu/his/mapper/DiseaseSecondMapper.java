package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiseaseSecondMapper extends MyMapper<DiseaseSecond> {

    //模糊搜索name
    public List<DiseaseSecond> findByName(String name);


    //获得所有疾病
    public List<DiseaseSecond>  getAll();

    //准确获得ICD
    DiseaseSecond findByIcdId(String icdId);

    //模糊搜索ICD
    List<DiseaseSecond> selectDiseaseByIcd(String icdID);

    //准确获得疾病代码
    DiseaseSecond findByDiseaseCoding(String diseaseCoding);

    //准确查询
    DiseaseSecond selectByName(String name);
}