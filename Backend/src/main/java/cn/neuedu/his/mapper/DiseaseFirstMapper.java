package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface DiseaseFirstMapper extends MyMapper<DiseaseFirst> {
    DiseaseFirst getDiseaseByname(String name);
}