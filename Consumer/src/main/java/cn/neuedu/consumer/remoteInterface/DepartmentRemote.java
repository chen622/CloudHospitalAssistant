package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DepartmentRemote {
    @GetMapping("/department/getByKind/{kindId}")
    JSONObject getByKind(@PathVariable("kindId") Integer kindId);

    @GetMapping("/department/get")
    JSONObject getDepartmentInformation();

    @PostMapping("/department/delete/{id}")
    JSONObject deleteDepartmentInformation(@PathVariable("id") Integer id);

    @PostMapping("/department/add")
    JSONObject addDepartment(@RequestBody JSONObject jsonObject);

    @PostMapping("/department/modify")
    JSONObject modifyDepartment(@RequestBody JSONObject jsonObject);

    @PostMapping("/department/retreat/{id}")
    JSONObject modifyDepartment(@PathVariable("id") Integer id);

    @GetMapping("/department/getAllDepartmentKind")
    JSONObject getAllDepartmentMatchKind();

    @GetMapping("/department/getDepartmentList/{name}")
    JSONObject getDepartmentListByname(@PathVariable(name = "name") String name);

    @GetMapping("/department/getDepartmentList/")
    JSONObject getDepartmentListByname();

    @PostMapping("/department/departmentClinicWorkload")
    JSONObject getClinicDepartmentWorkLoad(@RequestBody JSONObject jsonObject);

    @PostMapping("/department/departmentTechniqueWorkload")
    JSONObject getTechniqueDepartmentWorkLoad(@RequestBody JSONObject jsonObject);
}
