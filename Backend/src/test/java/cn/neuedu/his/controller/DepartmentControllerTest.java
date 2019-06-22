package cn.neuedu.his.controller;
import cn.neuedu.his.model.Diagnose;
import cn.neuedu.his.model.InspectionApplication;
import cn.neuedu.his.model.MedicalRecord;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.MedicalRecordService;
import cn.neuedu.his.service.RegistrationService;
import cn.neuedu.his.util.PermissionCheck;
import cn.neuedu.his.util.constants.Constants;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    RegistrationService registrationService;
    @Autowired
    MedicalRecordService medicalRecordService;
    private String token = "";

    @Before
    public void setUp() throws Exception {
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET)
                .setHeaderParam("typ", Constants.TOKEN_TYPE)
                .setIssuer(Constants.TOKEN_ISSUER)
                .setAudience(Constants.TOKEN_AUDIENCE)
                .setSubject("ccmccm")
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRY_TIME))
                .claim("id", 1)
                .claim("typeId", 606)
                .compact();
        this.token = Constants.TOKEN_PREFIX + token;
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new JwtCheckAuthorizationFilter()).build();
    }

    @Test
    public void select() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/department/select")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void getDepartmentById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/department/get/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteDepartmentById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/department/delete/14000")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("610"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addDepartment() throws Exception{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "sddd");
        jsonObject.put("kindId", 2);
        jsonObject.put("code", "sdsdsd");

        String request = jsonObject.toString();
        mockMvc.perform(MockMvcRequestBuilders.post("/department/add")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }



    @Test
    public void modifyDepartment() throws Exception{

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id",1);
        jsonObject.put("name", "账户sk");
        jsonObject.put("kindId", 1);
        jsonObject.put("code", "s");

        String request = jsonObject.toString();
        mockMvc.perform(MockMvcRequestBuilders.post("/department/modify")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getClinicDepartmentWorkLoad() throws Exception {
        JSONObject param = new JSONObject();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = new Date();
        Date end = new Date();
        try {
            start = formatter.parse("2019-05-29 06:00:00");
            end = formatter.parse("2019-05-30 12:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        param.put("start", start);
        param.put("end", end);
        String requestJson = param.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/department/departmentClinicWorkload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getTechniqueDepartmentWorkLoad() throws Exception {
        JSONObject param = new JSONObject();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = new Date();
        Date end = new Date();
        try {
            start = formatter.parse("2019-05-29 06:00:00");
            end = formatter.parse("2019-06-04 00:24:48");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        param.put("start", start);
        param.put("end", end);
        String requestJson = param.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.get("/department/departmentTechniqueWorkload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getDepartmentType() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/department/getAllDepartmentKind")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getDepartmentList() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.post("/department/delete/2")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

}
