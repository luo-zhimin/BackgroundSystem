package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.Size;
import com.background.system.mapper.SizeMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/10/3 12:52 PM
 */
@RequestMapping("test")
@RestController
public class test {

    @Resource
    private SizeMapper sizeMapper;

    int t = 100;
    int cn = 0;

    @RequestMapping("666")
    @IgnoreLogin
    public void go () {
        if (t > 0) {
            t--;
            cn++;
        }
        System.out.println(cn);
    }
}
