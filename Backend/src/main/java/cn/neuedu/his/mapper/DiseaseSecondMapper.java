package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DiseaseSecond;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DiseaseSecondMapper extends MyMapper<DiseaseSecond> {

    public List<DiseaseSecond> findByName(String name);
    public List<DiseaseSecond>  getAll();
}