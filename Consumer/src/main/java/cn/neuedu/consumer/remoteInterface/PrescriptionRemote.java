package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PrescriptionRemote {
    @PostMapping("/prescription/savePrescriptions")
    JSONObject savePrescriptions(@RequestBody JSONObject object);
}
