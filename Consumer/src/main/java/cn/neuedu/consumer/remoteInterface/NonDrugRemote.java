package cn.neuedu.consumer.remoteInterface;

import com.alibaba.fastjson.JSONObject;
import feign.Response;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

public interface NonDrugRemote {
    @PostMapping("/non_drug/insert")
    JSONObject insertNonDrug(@RequestBody JSONObject jsonObject);


    @GetMapping("/non_drug/selectByName/{name}")
    JSONObject selectNonDrugByName(@PathVariable("name") String name);

    @GetMapping("/non_drug/selectByCode/{code}")
    JSONObject selectNonDrugByCode(@PathVariable("code") String code);

    @PostMapping("/non_drug/delete/{id}")
    JSONObject deleteNonDrug(@PathVariable("id") Integer id);

    @PostMapping("/non_drug/modify")
    JSONObject modifyNonDrug(@RequestBody JSONObject jsonObject);


    @GetMapping("/non_drug/getTypeAndNonDrug/")
    JSONObject getTypeAndNonDrug();

    @GetMapping("/non_drug/excelOut")
    Response excelOut();

    @PostMapping("/non_drug/excelIn")
    public JSONObject excelIn(@RequestPart("file") MultipartFile excelFile);
}
