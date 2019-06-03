package cn.neuedu.his.mapper;

import cn.neuedu.his.model.DrugTemplate;
import cn.neuedu.his.model.Prescription;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component

public interface DrugTemplateMapper extends MyMapper<DrugTemplate> {
    List<DrugTemplate> getPrescriptionsTemByName(String name);
}