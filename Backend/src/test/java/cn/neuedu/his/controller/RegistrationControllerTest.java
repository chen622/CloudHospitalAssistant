package cn.neuedu.his.controller;

import cn.neuedu.his.mapper.RegistrationMapper;
import cn.neuedu.his.model.Patient;
import cn.neuedu.his.model.Registration;
import cn.neuedu.his.service.PatientService;
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

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.transaction.annotation.Transactional;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String token = "";

    @Autowired
    private RegistrationController registrationController;

    @Before
    public void setUp() throws Exception {
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET)
                .setHeaderParam("typ", Constants.TOKEN_TYPE)
                .setIssuer(Constants.TOKEN_ISSUER)
                .setAudience(Constants.TOKEN_AUDIENCE)
                .setSubject("Alex")
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRY_TIME))
                .claim("id", 8)
                .claim("typeId", 601)
                .compact();
        this.token = Constants.TOKEN_PREFIX + token;
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new JwtCheckAuthorizationFilter()).build();
    }

    @Test
    public void registration() throws Exception {
        JSONObject param = new JSONObject();
        param.put("scheduleId", 2);
        param.put("needBook", 1);
        param.put("patientId", 2);

        String requestJson = param.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/registration/registration")
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
    public void retreatRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registration/retreat/30")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Autowired
    RegistrationMapper mapper;
    @Autowired
    PatientService patientService;
    @Test
    public void getPatient() throws Exception{
       ArrayList<Registration> list= mapper.getPatient(1, "2019-01-01", "2019-11-01", 2,1 );

        Integer patientId=null;
        Patient patient=null;
        JSONArray a=new JSONArray();
        for(Registration registration:list){
            if(patientId==null || !patientId.equals(registration.getPatientId())){
                if(patient!=null){
                    a.add(patient);
                }
                patientId=registration.getPatientId();
                patient=patientService.findById(patientId);
                JSONArray array=new JSONArray();
                array.add(registration);
                patient.setRegistrations(array);
            }else{
                patient.addRegistrations(registration);
            }
        }
        if(patient!=null){
            a.add(patient);
        }
        System.out.println(a);
    }

}