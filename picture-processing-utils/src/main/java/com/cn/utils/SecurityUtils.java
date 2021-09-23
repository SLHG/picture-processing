package com.cn.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {

    public static final Map<String, String> AUTHORITY_MAP = new HashMap<String, String>() {{
        put("ADMIN", "ADMIN");
        put("USER", "USER");
    }};

    public static String getUserName(SecurityContext context) {
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public static boolean isAdmin(SecurityContext context) {
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public static String getUserAuthority(SecurityContext context) {
        UserDetails userDetails = (UserDetails) context.getAuthentication().getPrincipal();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String s = authority.getAuthority();
            return s.split("_")[1];
        }
        return "";
    }
}
