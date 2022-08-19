package com.background.system.controller;

import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.WechatUser;
import com.background.system.request.WechatUserInfo;
import com.background.system.service.WechatUserService;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

    @RequestMapping("/test")
    public String test() {
        return "success";
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public WechatUser selectOne(Long id) {
        return wechatUserService.selectByPrimaryKey(id);
    }

    @IgnoreLogin
    @GetMapping("/login")
    @ApiOperation("微信登陆")
    public Result<?> wechatLogin(@RequestParam("code") String code) {
        log.info("wechat login[{}]", code);
        return Result.success(wechatUserService.wechatLogin(code));
    }

    @PostMapping("/userInfo")
    @ApiOperation("更新用户信息")
    public Result<?> updateUserInfo(@RequestBody WechatUserInfo userInfo) {
        return Result.success(wechatUserService.updateUserInfo(userInfo));
    }
}
