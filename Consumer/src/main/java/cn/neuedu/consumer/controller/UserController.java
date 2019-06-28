package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.UserRemote;
import cn.neuedu.consumer.util.FeignRequestInterceptor;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Import(FeignClientsConfiguration.class)
public class UserController {
    private UserRemote userRemote;

    @Autowired
    public UserController(
            Decoder decoder, Encoder encoder, Client client) {
        this.userRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(UserRemote.class, "http://eureka-producer");
    }

    @GetMapping("/login")
    public JSONObject login(@RequestParam("username") String username, @RequestParam("password") String password) {
        return userRemote.login(username,password);
    }

    @GetMapping("/getDocByDept/{deptId}")
    public JSONObject getDocByDept(@PathVariable("deptId") Integer deptId) {
        return userRemote.getDocByDept(deptId);
    }

    @GetMapping("/function")
    public JSONObject login() {
        return userRemote.function();
    }

    @PostMapping("/register")
    public JSONObject register(@RequestBody JSONObject jsonObject) {
        return userRemote.register(jsonObject);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteUserInformation(@PathVariable("id") Integer id) {
        return userRemote.deleteUserInformation(id);
    }

    @PostMapping("/modify")
    public JSONObject modifyUserInformation(@RequestBody JSONObject jsonObject) {
        return userRemote.modifyUserInformation(jsonObject);
    }

    @PostMapping("/adminModify")
    public JSONObject adminModifyUserInformation(JSONObject jsonObject) {
        return userRemote.adminModifyUserInformation(jsonObject);
    }

    @GetMapping("/selectUser/{username}")
    public JSONObject selectUserInformation(@PathVariable("username") String username) {
        return userRemote.selectUserInformation(username);
    }

    @GetMapping("/adminSelectUser/{username}")
    public JSONObject adminSelectUserInformation(@PathVariable("username") String username) {
        return userRemote.adminSelectUserInformation(username);
    }


    @GetMapping("/findUser/{name}")
    public JSONObject findUser(@PathVariable("name") String name) {
        return userRemote.findUser(name);
    }

    @GetMapping("/findAll")
    public JSONObject findAll() {
        return userRemote.findAll();
    }

    @GetMapping("/getAllTollKeeper")
    public JSONObject getAllTollKeeper() {
        return userRemote.getAllTollKeeper();
    }

    @GetMapping("/getDoctorByDept")
    public JSONObject getDoctorByDept(@PathVariable("deptId") Integer deptId) {
        return userRemote.getDocByDept(deptId);
    }
}
