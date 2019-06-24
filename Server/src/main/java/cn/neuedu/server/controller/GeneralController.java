package cn.neuedu.server.controller;

import cn.neuedu.server.remoteInterface.GeneralRemote;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/general")
public class GeneralController {
    @Autowired
    private GeneralRemote generalRemote;

    @GetMapping("/getAllDepartmentKind")
    JSONObject getAllDepartmentKind() {
        return generalRemote.getAllDepartmentKind();
    }
}
