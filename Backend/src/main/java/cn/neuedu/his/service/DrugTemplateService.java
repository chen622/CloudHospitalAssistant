package cn.neuedu.his.service;
import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface DrugTemplateService extends Service<DrugTemplate> {
    List<DrugTemplate> getPrescriptionsTemByName(String name);
    @Transactional
    JSONObject deleteTem(Integer id);
}
