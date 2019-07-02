package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface InspectionTemplateRemote {
    @GetMapping("/IT/getIT")
    JSONObject getIT();

    @PostMapping("/IT/saveIT")
    JSONObject saveInspectionTem(@RequestBody JSONObject object);

    @GetMapping("/IT/getInspectionTemByName/{name}")
    JSONObject getInspectionTemByName(@PathVariable("name") String name);


    @PostMapping("/IT/updateInspectionTem")
    JSONObject updateInspectionTem(@RequestBody JSONObject object);

    @GetMapping("/IT/deleteIT/{id}")
    JSONObject deleteInspectionTemp(@PathVariable("id") Integer id);

}
