package cn.neuedu.his.service;
import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.util.inter.Service;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DiseaseFirstService extends Service<DiseaseFirst> {
    DiseaseFirst getDiseaseByname(String name);
}
