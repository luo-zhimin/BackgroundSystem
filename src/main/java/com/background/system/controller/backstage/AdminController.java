package com.background.system.controller.backstage;

import cn.hutool.jwt.JWTUtil;
import com.background.system.annotation.IgnoreLogin;
import com.background.system.constant.Constant;
import com.background.system.entity.Config;
import com.background.system.entity.token.Token;
import com.background.system.entity.vo.AdminLoginVo;
import com.background.system.exception.ServiceException;
import com.background.system.response.PictureResponse;
import com.background.system.service.PictureService;
import com.background.system.service.admin.IAdminUseService;
import com.background.system.service.impl.ConfigServiceImpl;
import com.background.system.util.ExcelUtil;
import com.background.system.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/17 6:40 PM
 */
@RestController
@RequestMapping("/admin")
@Api(tags = "后台管理")
public class AdminController {
    @Autowired
    private IAdminUseService adminUseService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private ConfigServiceImpl configService;

    /**
     * 登录请求
     * @param loginVo
     * @return
     */
    @IgnoreLogin
    @PostMapping("/login")
    public Result<Token> login(@RequestBody AdminLoginVo loginVo) {
        String userName = loginVo.getUsername();
        String password = loginVo.getPassword();
        if (StringUtils.isBlank(userName) || StringUtils.isBlank(password)) {
            throw new ServiceException(1000,"账号和密码都不能为空");
        }
        password = DigestUtils.md5Hex(password);
        Boolean loginFlag = adminUseService.queryUser(userName, password);
        if (!loginFlag) {
            throw new ServiceException(1001,"账号或密码错误");
        }
        Token token = createToken(userName, password);
        return Result.success(token);
    }

    private Token createToken(String userName, String password) {
        String tokenKey = userName + password;
        Map<String, Object> params = new HashMap<String, Object>(3) {
            private static final long serialVersionUID = 1L;

            {
                put("user_name", userName);
                put("password", password);
                put("expire_time", Constant.EXPIRE_TIME);
            }
        };
        String tokenValue = JWTUtil.createToken(params, tokenKey.getBytes());

        return Token.builder()
            .token(tokenValue)
            .expireTime(Constant.EXPIRE_TIME)
            .username(userName)
            .password(password)
            .build();
    }

    @PostMapping("/indexPicture/update")
    @ApiOperation("修改小程序首页图片")
    public Result<?> updateIndexPicture(@RequestBody PictureResponse request)
    {
        return Result.success(pictureService.updateIndexPicture(request));
    }

    @GetMapping("/config/export")
    @ApiOperation(value = "导出配置")
    public void export(Config config, HttpServletResponse response)
    {
        List<Config> list = configService.selectConfigList(config);
        ExcelUtil<Config> util = new ExcelUtil<Config>(Config.class);
        util.exportExcel(list, "config",response);
    }

}
