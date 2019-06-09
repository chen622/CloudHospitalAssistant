package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DiseaseFirst;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiseaseFirstMapper extends MyMapper<DiseaseFirst> {
    DiseaseFirst getDiseaseByname(String name);

    List<DiseaseFirst> getAllDiseaseType(Boolean authenication, String name);
}