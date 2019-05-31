package cn.neuedu.his.service;
import cn.neuedu.his.model.InspectionResult;
import cn.neuedu.his.util.inter.Service;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 *
 * Created by ccm on 2019/05/24.
 */
public interface InspectionResultService extends Service<InspectionResult> {
    JSONObject getInspectionResult(Integer id);
}
