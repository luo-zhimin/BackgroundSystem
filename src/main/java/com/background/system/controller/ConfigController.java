package com.background.system.controller;

import com.background.system.entity.Config;
import com.background.system.service.ConfigService;
import com.background.system.service.impl.ConfigServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (config)表控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    /**
     * 服务对象
     */
    @Resource
    private ConfigService configService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Config selectOne(Integer id) {
        return configService.selectByPrimaryKey(id);
    }

}
