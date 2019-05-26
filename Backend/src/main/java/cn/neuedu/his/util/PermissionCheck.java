package cn.neuedu.his.util;

import org.springframework.security.core.Authentication;

import java.util.Map;

public class PermissionCheck {
    public static Integer getIdByUser(Authentication authentication) {
        Map<String, Object> data = (Map<String, Object>) authentication.getCredentials();
        Integer typeId = (Integer) data.get("typeId");
        return (Integer) data.get("id");
    }
}
