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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DrugTest {

    @Autowired
    private MockMvc mockMvc;

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
                .claim("typeId", 603)
                .compact();
        this.token = Constants.TOKEN_PREFIX + token;
        //        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new JwtCheckAuthorizationFilter()).build();
    }

    //插入医生信息
    @Test
    public void takeDrug() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/drug/takeDrug/57/1")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    //插入医生信息
    @Test
    public void get() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/drug/getAllDrug")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

}
