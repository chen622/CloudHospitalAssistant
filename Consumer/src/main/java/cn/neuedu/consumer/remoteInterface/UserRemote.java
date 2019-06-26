package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserRemote {
    @GetMapping("/getDocByDept/{deptId}")
    JSONObject getDocByDept(@PathVariable("deptId") Integer deptId);

    @GetMapping("/function")
    JSONObject login();

    @PostMapping("/register")
    JSONObject register(@RequestBody JSONObject jsonObject);

    @PostMapping("/delete/{id}")
    JSONObject deleteUserInformation(@PathVariable("id") Integer id);

    @PostMapping("/modify")
    JSONObject modifyUserInformation(@RequestBody JSONObject jsonObject);

    @PostMapping("/adminModify")
    JSONObject adminModifyUserInformation(JSONObject jsonObject);

    @GetMapping("/selectUser/{username}")
    JSONObject selectUserInformation(@PathVariable("username") String username);


    @GetMapping("/adminSelectUser/{username}")
    JSONObject adminSelectUserInformation(@PathVariable("username") String username);


    @GetMapping("/findUser/{name}")
    JSONObject findUser(@PathVariable("name") String name);

    @GetMapping("/getAllTollKeeper")
    JSONObject getAllTollKeeper();

    @GetMapping("/getDoctorByDept")
    JSONObject getDoctorByDept(@PathVariable("deptId") Integer deptId);
}
