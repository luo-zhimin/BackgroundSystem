package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.WechatUser;
import com.background.system.service.WechatUserService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (wechat_user)表控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/wechat")
@Slf4j
@Api(tags = "小程序管理")
public class WechatUserController {
    /**
     * 服务对象
     */
    @Resource
    private WechatUserService wechatUserService;

    /**
     * 通过主键查询单条数据
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public WechatUser selectOne(Long id) {
        return wechatUserService.selectByPrimaryKey(id);
    }


    @IgnoreLogin
    @GetMapping("/login")
    public Result<?> wechatLogin(@RequestParam("code") String code) {
        log.info("wechat login[{}]",code);
        return Result.success(wechatUserService.wechatLogin(code));
    }
}
