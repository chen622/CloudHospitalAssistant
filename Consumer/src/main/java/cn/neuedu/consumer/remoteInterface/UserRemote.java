package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

public interface UserRemote {
    @GetMapping("/user/login")
    JSONObject login(@RequestParam("username") String username, @RequestParam("password") String password);

    @GetMapping("/user/getDocByDept/{deptId}")
    JSONObject getDocByDept(@PathVariable("deptId") Integer deptId);

    @GetMapping("/user/function")
    JSONObject function();

    @PostMapping("/user/register")
    JSONObject register(@RequestBody JSONObject jsonObject);

    @PostMapping("/user/delete/{id}")
    JSONObject deleteUserInformation(@PathVariable("id") Integer id);

    @PostMapping("/user/modify")
    JSONObject modifyUserInformation(@RequestBody JSONObject jsonObject);

    @PostMapping("/user/adminModify")
    JSONObject adminModifyUserInformation(JSONObject jsonObject);

    @GetMapping("/user/selectUser/{username}")
    JSONObject selectUserInformation(@PathVariable("username") String username);


    @GetMapping("/user/adminSelectUser/{username}")
    JSONObject adminSelectUserInformation(@PathVariable("username") String username);


    @GetMapping("/user/findUser/{name}")
    JSONObject findUser(@PathVariable("name") String name);

    @GetMapping("/user/getAllTollKeeper")
    JSONObject getAllTollKeeper();

    @GetMapping("/user/getDoctorByDept")
    JSONObject getDoctorByDept(@PathVariable("deptId") Integer deptId);

    @GetMapping("/user/findAll")
    JSONObject findAll();
}
