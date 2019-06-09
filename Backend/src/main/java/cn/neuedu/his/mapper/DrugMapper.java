package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Drug;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component

public interface DrugMapper extends MyMapper<Drug> {

    List<Drug> getDrugByName(String name);
    ArrayList<Drug> getAllDrug();
    ArrayList<Drug> getByCode(String code);
    ArrayList<Drug> getByName(String name);
    ArrayList<Drug> getDrugByPartName(String name);
}