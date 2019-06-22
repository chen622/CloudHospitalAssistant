package cn.neuedu.his.mapper;

import cn.neuedu.his.model.Diagnose;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

@Component
public interface DiagnoseMapper extends MyMapper<Diagnose> {
    void deleteByMRT(Integer id);
}