package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DepartmentKindRemote {
    @PostMapping("/department_kind/add")
    JSONObject addDepartmentKind(@RequestBody JSONObject jsonObject);

    @PostMapping("/department_kind/modify")
    JSONObject modifyDepartmentKinds(@RequestBody JSONObject jsonObject);

    @PostMapping("/department_kind/delete/{id}")
    JSONObject deleteDepartmentKinds(@PathVariable("id") Integer id);

    @GetMapping("/department_kind/getAll")
    JSONObject getDepartmentKindList();

    @GetMapping("/department_kind/getDepartmentKindAndDepartment")
    JSONObject getDepartmentKindAndDepartment();

    @GetMapping("/department_kind/getClinical")
    JSONObject getClinical();
}
