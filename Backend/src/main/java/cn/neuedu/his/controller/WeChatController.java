package cn.neuedu.his.controller;


import cn.neuedu.his.service.PatientService;
import cn.neuedu.his.util.CommonUtil;
import cn.neuedu.his.util.constants.ErrorEnum;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wechat")
public class WeChatController {
    @Autowired
    PatientService patientService;

    @PostMapping("/login")
    public JSONObject login(@RequestBody JSONObject requestJson) {
        try {
            JSONObject returnJson = patientService.parseMiniProgramLoginRequest(requestJson);
            return CommonUtil.successJson(returnJson);
        } catch (RuntimeException e) {
            return CommonUtil.errorJson(ErrorEnum.E_500);
        }
    }
}
