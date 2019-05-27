package cn.neuedu.his.controller;

import cn.neuedu.his.model.User;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject jsonObject, Authentication authentication) {
        return CommonUtil.successJson(authentication.getName());
    }


    @PostMapping("/register")
    public JSONObject register(@RequestBody User user) {
//        user.setId(1);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setIdentifyId("13020319980528000X");
//        user.setTypeId(41);
        userService.save(user);
        return CommonUtil.successJson(user);
    }

}
