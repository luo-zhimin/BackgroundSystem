package com.background.system.intercept;

import cn.hutool.jwt.JWTUtil;
import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.vo.AdminUserVO;
import com.background.system.exception.VerifyException;
import com.background.system.service.admin.IAdminUseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 请求拦截器
 *
 * @author meng
 */
@Slf4j
@Component
public class RequestInterceptHandle extends HandlerInterceptorAdapter {

    @Autowired
    private IAdminUseService adminUseService;

    private final String ALLOW_URL = "/error";
    private final String TOKEN_KEY = "Authorization";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod method = (HandlerMethod)handler;
        // 有IgnoreLogin注解修饰的方法不需要拦截
        IgnoreLogin ignoreLogin = method.getMethodAnnotation(IgnoreLogin.class);
        if (Objects.nonNull(ignoreLogin)) {
            return true;
        }
        // 错误页路径不需要拦截
        String url = request.getServletPath();
        if (url.contains(ALLOW_URL)) {
            return true;
        }
        String authorization = request.getHeader(TOKEN_KEY);
        if (StringUtils.isBlank(authorization)) {
            throw VerifyException.builder().code(200).exceptionMsg("请登录之后再操作").build();
        }
        AdminUserVO admin = adminUseService.getAdminInfo();
        String token_key = admin.getUserName() + admin.getPassword();
        // 校验token
        boolean verify = false;
        try {
            verify = JWTUtil.verify(authorization, token_key.getBytes());
        } catch (Exception e) {
            throw VerifyException.builder().code(200).exceptionMsg("token无效请重新登录").build();
        }
        if (!verify) {
            throw VerifyException.builder().code(200).exceptionMsg("token无效请重新登录").build();
        }
        return true;
    }
}
