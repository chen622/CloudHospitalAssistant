package cn.neuedu.his.controller;

import cn.neuedu.his.model.User;
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
public class UserControllerTest {

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


    //插入医生信息
    @Test
    public void insertProperDoctorrInformation() throws Exception {
        //json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "t");
        jsonObject.put("realName", "tys");
        jsonObject.put("password", "123456");
        jsonObject.put("createTime", new Date(System.currentTimeMillis()));
        jsonObject.put("typeId", 605);
        jsonObject.put("departmentId", 1);
        jsonObject.put("identifyId", "211002199709251979");
        jsonObject.put("titleId", 701);
        jsonObject.put("canArrange", false);

        String request = jsonObject.toJSONString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    //插入普通用户信息
    @Test
    public void insertProperNormalUserInformation() throws Exception {
        //json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "ys");
        jsonObject.put("realName", "tys");
        jsonObject.put("password", "123456");
        jsonObject.put("createTime", new Date(System.currentTimeMillis()));
        jsonObject.put("typeId", 601);
        jsonObject.put("departmentId", 1);
        jsonObject.put("identifyId", "211002199709251979");
        jsonObject.put("titleId", 701);
        jsonObject.put("canArrange", false);

        String request = jsonObject.toJSONString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    //插入重复用户信息
    @Test
    public void insertSameUserInformation() throws Exception {
        //json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "t");
        jsonObject.put("realName", "tys");
        jsonObject.put("password", "123456");
        jsonObject.put("createTime", new Date(System.currentTimeMillis()));
        jsonObject.put("typeId", 602);
        jsonObject.put("departmentId", 1);
        jsonObject.put("identifyId", "211002199709251979");
        jsonObject.put("titleId", 701);
        jsonObject.put("canArrange", false);

        String request = jsonObject.toJSONString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("600"))
                .andDo(MockMvcResultHandlers.print());
    }

    //插入错误身份证信息
    @Test
    public void insertUserInformationWithWrongID() throws Exception {
        //json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "y");
        jsonObject.put("realName", "tys");
        jsonObject.put("password", "123456");
        jsonObject.put("createTime", new Date(System.currentTimeMillis()));
        jsonObject.put("typeId", 602);
        jsonObject.put("departmentId", 1);
        jsonObject.put("identifyId", "2110");
        jsonObject.put("titleId", 701);
        jsonObject.put("canArrange", false);

        String request = jsonObject.toJSONString();

        //测试小于18位的身份证信息
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("501"))
                .andDo(MockMvcResultHandlers.print());

        //测试大于18位的身份证信息
        jsonObject.put("identifyId", "211022222222222222222222222222222222222");
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("501"))
                .andDo(MockMvcResultHandlers.print());

    }

    //插入错误部门的用户信息
    @Test
    public void insertUserInformationWithWrongDepartment() throws Exception {
        //json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "y");
        jsonObject.put("realName", "tys");
        jsonObject.put("password", "123456");
        jsonObject.put("createTime", new Date(System.currentTimeMillis()));
        jsonObject.put("typeId", 602);
        jsonObject.put("departmentId", 1000);
        jsonObject.put("identifyId", "211002199709251979");
        jsonObject.put("titleId", 701);
        jsonObject.put("canArrange", false);

        String request = jsonObject.toJSONString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


    //插入错误类型的用户信息
    @Test
    public void insertUserInformationWithWrongType() throws Exception {
        //json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "y");
        jsonObject.put("realName", "tys");
        jsonObject.put("password", "123456");
        jsonObject.put("createTime", new Date(System.currentTimeMillis()));
        jsonObject.put("typeId", 60200);
        jsonObject.put("departmentId", 1000);
        jsonObject.put("identifyId", "211002199709251979");
        jsonObject.put("titleId", 701);
        jsonObject.put("canArrange", false);

        String request = jsonObject.toJSONString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(request)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("501"))
                .andDo(MockMvcResultHandlers.print());
    }


    //插入错误类型的用户信息（未完成）
    @Test
    public void deleteUserInformation() throws Exception {

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET)
                .setHeaderParam("tp", Constants.TOKEN_TYPE)
                .setIssuer(Constants.TOKEN_ISSUER)
                .setAudience(Constants.TOKEN_AUDIENCE)
                .setSubject("ccmccm")
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRY_TIME))
                .claim("id", 1)
                .claim("typeId", 606)
                .compact();
        this.token = Constants.TOKEN_PREFIX + token;



        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("t")
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("501"))
                .andDo(MockMvcResultHandlers.print());
    }

    //插入医生信息
    @Test
    public void modifyProperDoctorInformation() throws Exception {
        //json数据
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userName", "t");
        jsonObject.put("realName", "ty");
        jsonObject.put("password", "123456");
        jsonObject.put("createTime", new Date(System.currentTimeMillis()));
        jsonObject.put("typeId", 602);
        jsonObject.put("departmentId", 1);
        jsonObject.put("identifyId", "211002199709251978");
        jsonObject.put("titleId", 702);
        jsonObject.put("canArrange", true);

        String request = jsonObject.toJSONString();

        mockMvc.perform(MockMvcRequestBuilders.post("/user/modify")
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
    public void selectAdminUserInformation() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/adminSelectUser/ccmccm")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("username")
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    
    @Test
    public void selectUserInformation() throws Exception {

        String requestJson ="ccmccm";
        mockMvc.perform(MockMvcRequestBuilders.get("/user/selectUser/ccmccm")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content("username")
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findAll() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/user/findAll")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header(Constants.TOKEN_HEADER, token)
                .accept(MediaType.APPLICATION_JSON_UTF8)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("100"))
                .andDo(MockMvcResultHandlers.print());
    }


}


