package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DailySettleRemote {
    @PostMapping("/daily_settle/produceSettleInfo")
    JSONObject produceSettleInfo(@RequestBody JSONObject jsonObject);

    @PostMapping("/daily_settle/makeTable")
    JSONObject makeDailySettleTable(@RequestBody JSONObject jsonObject);

    @GetMapping("/daily_settle/getSettleInfo/{adminId}")
    JSONObject getSettleInfo(@PathVariable("adminId") Integer adminId);

    @PostMapping("/daily_settle/check/{settleId}")
    JSONObject checkPass(@PathVariable("settleId") Integer settleId);

    @GetMapping("/daily_settle/initialize")
    JSONObject initializePage();
}
