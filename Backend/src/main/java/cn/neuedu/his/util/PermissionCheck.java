package cn.neuedu.his.util;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;

import java.util.Map;

/**
 * 权限校验类
 */
public class PermissionCheck {
    /**
     * 通用的权限校验接口，所有类别都可通过
     *
     * @param authentication
     * @return
     */
    public static Integer getIdByUser(Authentication authentication) {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        return (Integer) data.get("id");
    }

    /**
     * 挂号员权限检验
     * @param authentication
     * @return
     * @throws AuthenticationServiceException
     */
    public static Integer getIdByRegistrar(Authentication authentication) throws AuthenticationServiceException{
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        if (typeId == 601) {
            return (Integer) data.get("id");
        } else {
            throw new AuthenticationServiceException("");
        }
    }
}
