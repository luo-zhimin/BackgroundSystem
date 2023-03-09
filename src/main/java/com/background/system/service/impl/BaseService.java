package com.background.system.service.impl;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.background.system.constant.Constant;
import com.background.system.entity.token.Token;
import com.background.system.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/20 12:08
 */
public abstract class BaseService<T> {

    /**
     * 初始化page
     * @param page 页数
     * @param size 条数
     * @return page
     */
//    @SuppressWarnings("all")
    public Page<T> initPage(Integer page,Integer size){
        Page<T> initPage = new Page<>();
        initPage.setPages(page);
        initPage.setSize(size);
        return initPage;
    }


    /**
     * 获取小程序当前登录用户信息
     * @return token->userName
     */
    public Token getWeChatCurrentUser() {
        //拦截器已经校验 这里不进行校验
        JWT currentJwt = getCurrentJwt();
        return Token.builder()
                .username(currentJwt.getPayload(Constant.WX_TOKEN_KEY).toString())
                .type(1)
                .build();
    }


    /**
     * 获取后台当前登录用户信息
     * @return token->userName
     */
    public Token getCurrentUser() {
        //拦截器已经校验 这里不进行校验
        JWT currentJwt = getCurrentJwt();
        return Token.builder()
                .username(currentJwt.getPayload(Constant.USER_NAME).toString())
                .type(2)
                .build();
    }

    private HttpServletRequest request() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }


    private JWT getCurrentJwt(){
        String authorization = request().getHeader(Constant.TOKEN_KEY);
        if (StringUtils.isEmpty(authorization)) {
            throw new ServiceException(403, "登陆过期，请重新登陆");
        }
        return JWTUtil.parseToken(authorization);
    }
}
