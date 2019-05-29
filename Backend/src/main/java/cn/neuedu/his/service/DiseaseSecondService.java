package cn.neuedu.his.service;
import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.util.inter.Service;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DiseaseSecondService extends Service<DiseaseSecond> {
    public List<DiseaseSecond> findByName(String name);
    public List<DiseaseSecond>  getAll();
}
