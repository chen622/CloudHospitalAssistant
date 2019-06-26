package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.DailySettleRemote;
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

import java.util.ArrayList;

@RestController
@RequestMapping("/daily_settle")
@Import(FeignClientsConfiguration.class)
public class DailySettleController {
    private DailySettleRemote dailySettleRemote;

    @Autowired
    public DailySettleController(
            Decoder decoder, Encoder encoder, Client client) {
        this.dailySettleRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(DailySettleRemote.class, "http://eureka-producer");
    }

    @PostMapping("/produceSettleInfo")
    public JSONObject produceSettleInfo(@RequestBody JSONObject jsonObject) {
        return dailySettleRemote.produceSettleInfo(jsonObject);
    }

    @PostMapping("/makeTable")
    public JSONObject makeDailySettleTable(@RequestBody JSONObject jsonObject) {
        return dailySettleRemote.makeDailySettleTable(jsonObject);
    }

    @GetMapping("/getSettleInfo/{adminId}")
    public JSONObject getSettleInfo(@PathVariable("adminId") Integer adminId) {
        return dailySettleRemote.getSettleInfo(adminId);
    }

    @PostMapping("/check/{settleId}")
    public JSONObject checkPass(@PathVariable("settleId") Integer settleId) {
        return dailySettleRemote.checkPass(settleId);
    }

    @GetMapping("/initialize")
    public JSONObject initializePage() {
        return dailySettleRemote.initializePage();
    }
}
