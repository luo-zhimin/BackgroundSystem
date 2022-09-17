package com.background.system.intercept;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.background.system.annotation.IgnoreLogin;
import com.background.system.constant.Constant;
import com.background.system.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.background.system.constant.Constant.SWAGGER_TYPE;

/**
 * 请求拦截器
 *
 * @author meng
 */
@Slf4j
@Component
public class RequestInterceptHandle extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod method = (HandlerMethod) handler;
        // 有IgnoreLogin注解修饰的方法不需要拦截
        IgnoreLogin ignoreLogin = method.getMethodAnnotation(IgnoreLogin.class);
        if (Objects.nonNull(ignoreLogin)) {
            return true;
        }
        // 错误页路径不需要拦截
        String url = request.getServletPath();
        if (url.contains(Constant.ALLOW_URL) || url.contains(SWAGGER_TYPE)) {
            return true;
        }
        String authorization = request.getHeader(Constant.TOKEN_KEY);
        if (StringUtils.isBlank(authorization)) {
            throw new ServiceException(403,"请登录之后再操作");
//            throw VerifyException.builder().code(200).exceptionMsg("请登录之后再操作").build();
        }

        // 校验token
        boolean verify;
        JWT jwt = JWTUtil.parseToken(authorization);
        // 管理后台请求token校验
        if (url.contains(Constant.ADMIN_REQUEST_TYPE)) {
            try {
                String userName = jwt.getPayload(Constant.USER_NAME).toString();
                String password = jwt.getPayload(Constant.PASSWORD).toString();
                String tokenKey = userName + password;
                verify = JWTUtil.verify(authorization, tokenKey.getBytes());
            } catch (Exception e) {
                throw new ServiceException(403,"token无效请重新登录");
//                throw VerifyException.builder().code(200).exceptionMsg("token无效请重新登录").build();
            }
        } else {
            String openId = jwt.getPayload(Constant.WX_TOKEN_KEY).toString();
            verify = JWTUtil.verify(authorization,openId.getBytes(StandardCharsets.UTF_8));
            //小程序token 过期 需要续期
            if (verify){
                jwt.setPayload("expire_time",Constant.EXPIRE_TIME);
            }
        }

        if (!verify) {
            throw new ServiceException(403,"token无效请重新登录");
//            throw VerifyException.builder().code(200).exceptionMsg("token无效请重新登录").build();
        }
        return true;
    }
}
