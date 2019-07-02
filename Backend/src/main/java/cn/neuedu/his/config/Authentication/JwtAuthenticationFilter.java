package cn.neuedu.his.config.Authentication;

import cn.neuedu.his.util.constants.Constants;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccm
 * Jwt进行授权的拦截器
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private Logger logger = Logger.getLogger(JwtAuthenticationFilter.class);

    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;

        setFilterProcessesUrl(Constants.AUTH_LOGIN_URL);
    }

    /**
     * 授予权限接口
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     * 生成token
     *
     * @param request
     * @param response
     * @param filterChain
     * @param authentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) {
        UserDetails user = ((UserDetails) authentication.getPrincipal());

//        List<String> roles = user.getAuthorities()
//                .stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());


        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, Constants.JWT_SECRET)//密钥
                .setHeaderParam("typ", Constants.TOKEN_TYPE)//类别
                .setIssuer(Constants.TOKEN_ISSUER)
                .setAudience(Constants.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())//标识
                .setExpiration(new Date(System.currentTimeMillis() + Constants.EXPIRY_TIME))//过期时间
                .claim("id", user.getId())
                .claim("typeId", user.getTypeId())
                .compact();

        try {
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json;charset=UTF-8");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(Constants.TOKEN_HEADER, Constants.TOKEN_PREFIX + token);
            jsonObject.put("userType", user.getTypeId());
            writer.print(jsonObject.toJSONString());
            writer.close();
        } catch (IOException e) {
            logger.error(e);
        }
        response.addHeader(Constants.TOKEN_HEADER, Constants.TOKEN_PREFIX + token);
        response.addHeader("usertype", user.getTypeId().toString());
        response.setHeader("Access-Control-Expose-Headers", Constants.TOKEN_HEADER + "," + "usertype");
    }
}
