package com.vnu.server.common;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class FunctionPopular {
    public static String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
