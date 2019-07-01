package cn.neuedu.his.controller;

import cn.neuedu.his.util.constants.Constants;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NoDrugControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private String token = "";

    @Autowired
    InvoiceController invoiceController;

    @Before
    public void setUp() throws Exception {
        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET)
                .setHeaderParam("typ", Constants.TOKEN_TYPE)
                .setIssuer(Constants.TOKEN_ISSUER)
                .setAudience(Constants.TOKEN_AUDIENCE)
                .setSubject("Alex")
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRY_TIME))
                .claim("id", 2)
                .claim("typeId", 606)
                .compact();
        this.token = Constants.TOKEN_PREFIX + token;
//        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new JwtCheckAuthorizationFilter()).build();
    }

    @Test
    public void printVoice() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/non_drug/selectByName/X清肌酸激酶－MB同工酶活性测定（干化X法）")
                .contentType(MediaType.APPLICATION_JSON)
                //.content(requestJson)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getTypeAndNonDrugTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/non_drug/getTypeAndNonDrug/nameAndCode/干化X法/D（GHXF）")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    @Test
    public void insert() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code","3232323");
        jsonObject.put("name","232323");
        jsonObject.put("price",232.22);
        jsonObject.put("feeTypeId",108);
        jsonObject.put("executiveDepartmrntId",1);

        String param = jsonObject.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/non_drug/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(param)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

}