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
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.minidev.json.JSONObject;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DoctorControllerTest {

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
                .claim("typeId", Constants.OUT_PATIENT_DOCTOR)
                .compact();
        this.token = Constants.TOKEN_PREFIX + token;
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new JwtCheckAuthorizationFilter()).build();
    }

    @Test
    public void getAllRecordByPatientId() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getAllRecord/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void getRegistrationByPatientName() throws Exception , AuthenticationServiceException {
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getByName/"+"小红")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void getAllWaitingRegistration() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getAllWait/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    //检查模板
    @Test
    public void getHospitalCheckTemps() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getHospitalCheckTemps")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getDeptCheckTemps() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getDeptCheckTemps")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getPersonalCheckTemps() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getPersonalInspectionTemps")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    //病例模板
    @Test
    public void getHospitalMR() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getHospitalMR")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getDeptMR() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getDeptMR")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getPersonalMR() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getPersonalMR")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findDiseaseByName() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/findDisease/霍乱")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getAllDiseases() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getAllDiseases")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void findNonDrugByName() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/findNonDrug/蛋白")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getAllNonDrug() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/doctor/getAllNonDrug")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void setFirstDiagnose() throws Exception, AuthenticationServiceException {
        MedicalRecord medicalRecord = medicalRecordService.findById(1);
        medicalRecord.setId(null);
        JSONObject object=new JSONObject();
        object.put("medicalRecord", medicalRecord);
        object.put("registrationID", 1);
        Diagnose diagnose=new Diagnose();
        diagnose.setDiseaseId(1);
        object.put("diagnose",diagnose );
        String requestJson=object.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/doctor/firstDiagnose")
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
    public void saveHospitalMRTemplate() throws Exception, AuthenticationServiceException {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordWithDiagnose(1);
        JSONObject object=new JSONObject();
        object.put("medicalRecord", medicalRecord);
        object.put("name", "测试存入全院模板");
        String  requestJson=object.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/doctor/saveHospitalMRTemplate")
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
    public void saveDeptMRTemplate() throws Exception, AuthenticationServiceException {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordWithDiagnose(1);
        JSONObject object=new JSONObject();
        object.put("medicalRecord", medicalRecord);
        object.put("name", "测试存入科室模板");
        String  requestJson=object.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/doctor/saveDeptMRTemplate")
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
    public void savePersonalMRTemplate() throws Exception, AuthenticationServiceException {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecordWithDiagnose(1);
        JSONObject object=new JSONObject();
        object.put("medicalRecord", medicalRecord);
        object.put("name", "测试存入个人模板");
        String  requestJson=object.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/doctor/savePersonalMRTemplate")
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
    public void saveInspection() throws Exception {
        InspectionApplication i = new InspectionApplication();
        i.setQuantity(1);
        i.setNonDrugId(1);

        InspectionApplication i1 = new InspectionApplication();
        i1.setQuantity(2);
        i1.setNonDrugId(2);

        JSONArray array=new JSONArray();
        array.add(i);
        array.add(i1);
        JSONObject object=new JSONObject();
        object.put("medicalRecordId", 1);
        object.put("inspections", array);
        String  requestJson=object.toJSONString();

        mockMvc.perform(MockMvcRequestBuilders.post("/doctor/saveInspection")
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
    public void saveInspectionTem() throws Exception {
        InspectionApplication i = new InspectionApplication();
        i.setQuantity(1);
        i.setNonDrugId(3);

        InspectionApplication i1 = new InspectionApplication();
        i1.setQuantity(2);
        i1.setNonDrugId(4);

        JSONArray array=new JSONArray();
        array.add(i);
        array.add(i1);
        JSONObject object=new JSONObject();
        object.put("name", "测试检查项目作为模板");
        object.put("applications", array);
        object.put("isNew", false);
        object.put("level",Constants.PERSONALLEVEL);
        String  requestJson=object.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/doctor/saveInspectionTem")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


}