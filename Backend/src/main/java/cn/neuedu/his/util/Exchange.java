package cn.neuedu.his.util;

import cn.neuedu.his.model.Patient;
import cn.neuedu.his.util.constants.Constants;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class Exchange {

    @Autowired
    RestTemplate restTemplate;

    public Patient code2Session(String code) throws RuntimeException {
        Patient customer = new Patient();
        Map<String, String> vars = new HashMap<String, String>();
        vars.put("appId", Constants.APP_ID);
        vars.put("secretKey", Constants.SECRIT_KEY);
        vars.put("code", code);
        String s = restTemplate.getForObject("https://api.weixin.qq.com/sns/jscode2session?appid={appId}&secret={secretKey}&js_code={code}&grant_type=authorization_code", String.class, vars);
        JSONObject result = JSONObject.parseObject(s);
        System.out.println(result);
        String openId = result.getString("openid");
        if ((!result.containsKey("errcode")) && openId != null) {
            // 成功
            customer.setOpenId(openId);
            customer.setSessionKey(result.getString("session_key"));
            return customer;
        } else {
            throw new RuntimeException();
        }
    }
}
