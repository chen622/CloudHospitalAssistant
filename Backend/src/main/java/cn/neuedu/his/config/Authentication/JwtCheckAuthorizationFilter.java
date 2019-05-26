package cn.neuedu.his.config.Authentication;

import cn.neuedu.his.util.StringUtils;
import cn.neuedu.his.util.constants.Constants;
import io.jsonwebtoken.*;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ccm
 * 鉴权拦截器
 */
public class JwtCheckAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger log = Logger.getLogger(JwtCheckAuthorizationFilter.class);

    public JwtCheckAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        String header = request.getHeader(Constants.TOKEN_HEADER);

        if (StringUtils.isEmpty(header) || !header.startsWith(Constants.TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(Constants.TOKEN_HEADER);
        if (!StringUtils.isEmpty(token)) {
            try {
                Claims body = Jwts.parser()
                        .setSigningKey(Constants.JWT_SECRET)
                        .parseClaimsJws(token.replace("Bearer ", "")).getBody();
                String username = body.getSubject();
                Map<String, Object> data = new HashMap<>();
                data.put("id", body.get("id"));
                data.put("typeId", body.get("typeId"));

//                List<SimpleGrantedAuthority> authorities = ((List<?>) parsedToken.getBody()
//                        .get("rol")).stream()
//                        .map(authority -> new SimpleGrantedAuthority((String) authority))
//                        .collect(Collectors.toList());

                if (!StringUtils.isEmpty(username)) {
                    return new UsernamePasswordAuthenticationToken(username, data, new ArrayList<>());
                }
            } catch (ExpiredJwtException exception) {
                log.warn(String.format("Request to parse expired JWT : %s failed : %s", token, exception.getMessage()));
            } catch (UnsupportedJwtException exception) {
                log.warn(String.format("Request to parse unsupported JWT : %s failed : %s", token, exception.getMessage()));
            } catch (MalformedJwtException exception) {
                log.warn(String.format("Request to parse invalid JWT : %s failed : %s", token, exception.getMessage()));
            } catch (SignatureException exception) {
                log.warn(String.format("Request to parse JWT with invalid signature : %s failed : %s", token, exception.getMessage()));
            } catch (IllegalArgumentException exception) {
                log.warn(String.format("Request to parse empty or null JWT : %s failed : %s", token, exception.getMessage()));
            }
        }

        return null;
    }
}
