package cn.neuedu.his.service;
import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DiseaseFirstService extends Service<DiseaseFirst> {
    DiseaseFirst getDiseaseByname(String name);

    void insertDiseaseFirst(DiseaseFirst diseaseFirst);

    void modifyDiseaseFirst(DiseaseFirst diseaseFirst);

    void deleteDiseaseFirst(Integer id);

    DiseaseFirst selectDiseaseFirst(String name);//根据准确姓名获取

    List<DiseaseFirst> getAllDiseaseType(Boolean authenication, String name);//可以模糊查询


}
