package com.snow.audit.config;

import com.snow.audit.common.SkipAuth;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class ApiKeyInterceptor extends HandlerInterceptorAdapter {

    @Value("${api.keys}")
    private String apiKeys;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 类或方法上有 @SkipAuth 注解则跳过鉴权
        if (handlerMethod.getMethodAnnotation(SkipAuth.class) != null
                || handlerMethod.getBeanType().getAnnotation(SkipAuth.class) != null) {
            return true;
        }

        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"缺少Authorization头或格式错误，请使用 Bearer <api-key>\"}");
            return false;
        }

        String token = authHeader.substring(7);
        if (!isValidToken(token)) {
            response.setStatus(403);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"message\":\"无效的API Key\"}");
            return false;
        }

        return true;
    }

    private boolean isValidToken(String token) {
        if (StringUtils.isBlank(apiKeys)) {
            return false;
        }
        Set<String> keySet = new HashSet<>(Arrays.asList(apiKeys.split(",")));
        return keySet.contains(token);
    }
}
