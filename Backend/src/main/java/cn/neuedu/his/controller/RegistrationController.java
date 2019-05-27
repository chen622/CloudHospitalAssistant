package cn.neuedu.his.controller;

import cn.neuedu.his.model.Registration;
import cn.neuedu.his.model.User;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.service.UserService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Created by ccm on 2019/05/24.
 */
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;
    @Autowired
    UserService userService;

    /**
     * 现场挂号
     * @param jsonObject
     * @return
     */
    @Transactional
    @PostMapping("/registration")
    public JSONObject registration(@RequestBody JSONObject jsonObject, Authentication authentication) {
        PermissionCheck.getIdByUser(authentication);
        Registration registration = (Registration) JSONObject.parse(jsonObject.getJSONObject("Registration").toJSONString());
        User user = (User) JSONObject.parse(jsonObject.getJSONObject("User").toJSONString());
        if(registration == null || user == null) {
            return CommonUtil.errorJson(ErrorEnum.E_501);
        }
        registrationService.save((Registration)jsonObject.get("Registration"));
        userService.save((User) jsonObject.get("User"));

        return CommonUtil.successJson();
    }
}
