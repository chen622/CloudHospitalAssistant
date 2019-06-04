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
public class RegistriationTypeControllerTest {


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
                .claim("typeId", 606)
                .compact();
        this.token = Constants.TOKEN_PREFIX + token;
        //        mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilter(new JwtCheckAuthorizationFilter()).build();
    }


    @Test
    public void InsertRegistrationTypeTest() throws Exception {
        JSONObject param = new JSONObject();
        param.put("name", "高");
        param.put("is_default", false);
        param.put("price", 100.00);
        param.put("display_sequence_number",100);

        String requestJson = param.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/registration_type/insertRegisterType")
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
    public void deleteRegistrationTypeTest() throws Exception {
        JSONObject param = new JSONObject();
        param.put("name", "高级号");
        param.put("is_default", false);
        param.put("price", 11.00);
        param.put("display_sequence_number",100);

        String requestJson = param.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/registration_type/deleteRegisterType/高级号")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void modifyRegistrationTypeTest() throws Exception {
        JSONObject param = new JSONObject();
        param.put("name", "高级号");
        param.put("is_default", false);
        param.put("price", 11.00);
        param.put("display_sequence_number",100);

        String requestJson = param.toJSONString();
        mockMvc.perform(MockMvcRequestBuilders.post("/registration_type/deleteRegisterType/")
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
    public void selectRegistrationTypeTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/registration_type/selectRegisterType/高")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }
}
