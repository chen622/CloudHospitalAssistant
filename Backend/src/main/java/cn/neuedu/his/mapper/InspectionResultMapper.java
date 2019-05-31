package cn.neuedu.his.mapper;

import cn.neuedu.his.model.InspectionResult;
import cn.neuedu.his.util.inter.MyMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component


public interface InspectionResultMapper extends MyMapper<InspectionResult> {
    public  List<InspectionResult> getInspectionResult(Integer id);
}