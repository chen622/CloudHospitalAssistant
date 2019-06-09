package cn.neuedu.his.service;
import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DiseaseSecondService extends Service<DiseaseSecond> {

    List<DiseaseSecond> findByName(String name);
    List<DiseaseSecond>  getAll();
    List<DiseaseSecond> findByIcdId(String icdId);
    DiseaseSecond findByDiseaseCoding(String diseaseCoding);

    void insertDiseaseSecond(DiseaseSecond diseaseSecond);

    void delateDiseaseSecond(Integer id);

    void modifyDiseaseSecond(DiseaseSecond diseaseSecond);

    List<DiseaseSecond> findByDiseaseFirstid(Boolean authenication,Integer type);


}
