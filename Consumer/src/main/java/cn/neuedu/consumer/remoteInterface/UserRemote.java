package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

public interface UserRemote {
    @GetMapping("/user/login")
    JSONObject login(@RequestParam("username") String username,@RequestParam("password") String password);

    @GetMapping("/getDocByDept/{deptId}")
    JSONObject getDocByDept(@PathVariable("deptId") Integer deptId);

    @GetMapping("/function")
    JSONObject function();

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
