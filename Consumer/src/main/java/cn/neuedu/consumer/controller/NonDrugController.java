package cn.neuedu.consumer.controller;

import cn.neuedu.consumer.remoteInterface.NonDrugRemote;
import cn.neuedu.consumer.util.FeignRequestInterceptor;
import com.alibaba.fastjson.JSONObject;
import feign.Client;
import feign.Feign;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/non_drug")
@Import(FeignClientsConfiguration.class)
public class NonDrugController {
    private NonDrugRemote nonDrugRemote;

    private NonDrugRemote fileTransferRemote;

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired
    public NonDrugController(
            Decoder decoder, Encoder encoder, Client client) {
        this.nonDrugRemote = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(NonDrugRemote.class, "http://eureka-producer");
        this.fileTransferRemote = Feign.builder().client(client)
                .encoder(new SpringFormEncoder(new SpringEncoder(messageConverters)))
                .decoder(decoder)
                .contract(new SpringMvcContract())
                .requestInterceptor(new FeignRequestInterceptor())
                .target(NonDrugRemote.class, "http://eureka-producer");

    }

    @PostMapping("/insert")
    public JSONObject insertNonDrug(@RequestBody JSONObject jsonObject) {
        return nonDrugRemote.insertNonDrug(jsonObject);
    }


    @GetMapping("/selectByName/{name}")
    public JSONObject selectNonDrugByName(@PathVariable("name") String name) {
        return nonDrugRemote.selectNonDrugByName(name);
    }

    @GetMapping("/selectByCode/{code}")
    public JSONObject selectNonDrugByCode(@PathVariable("code") String code) {
        return nonDrugRemote.selectNonDrugByCode(code);
    }

    @PostMapping("/delete/{id}")
    public JSONObject deleteNonDrug(@PathVariable("id") Integer id) {
        return nonDrugRemote.deleteNonDrug(id);
    }

    @PostMapping("/modify")
    public JSONObject modifyNonDrug(@RequestBody JSONObject jsonObject) {
        return nonDrugRemote.modifyNonDrug(jsonObject);
    }


    @GetMapping("/getTypeAndNonDrug/")
    JSONObject getTypeAndNonDrug() {
        return nonDrugRemote.getTypeAndNonDrug();
    }

    @GetMapping("/excelOut")
    public String excelOut(HttpServletResponse response) {
        Response getResponse = nonDrugRemote.excelOut();
        Response.Body body = getResponse.body();
        try (InputStream inputStream = body.asInputStream();
             ServletOutputStream out = response.getOutputStream()) {
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;fileName=" + "nondrug.xlsx");
            out.write(b);
            return "success!";
        } catch (IOException e) {
            return "error!";
        }
    }

    @PostMapping("/excelIn")
    public JSONObject excelIn(@RequestPart("file") MultipartFile excelFile) {
        return fileTransferRemote.excelIn(excelFile);
    }

    @GetMapping("/getNonDrugByType")
    JSONObject getNonDrugByType(){
        return nonDrugRemote.getNonDrugByType();
    }
}
