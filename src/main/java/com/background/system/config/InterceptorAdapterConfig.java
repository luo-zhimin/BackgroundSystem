package com.background.system.config;

import com.background.system.intercept.RequestInterceptHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 *
 * @author meng
 */
@Configuration
public class InterceptorAdapterConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private RequestInterceptHandle requestInterceptHandle;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自己的拦截器并设置拦截的请求路径
        registry.addInterceptor(requestInterceptHandle).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
