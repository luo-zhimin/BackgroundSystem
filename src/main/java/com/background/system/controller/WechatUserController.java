package com.background.system.controller;
import com.background.system.entity.WechatUser;
import com.background.system.service.impl.WechatUserServiceImplImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
* (wechat_user)表控制层
*
* @author xxxxx
*/
@RestController
@RequestMapping("/wechat_user")
public class WechatUserController {
/**
* 服务对象
*/
@Resource
private WechatUserServiceImpl wechatUserServiceImpl;

/**
* 通过主键查询单条数据
*
* @param id 主键
* @return 单条数据
*/
@GetMapping("selectOne")
public WechatUser selectOne(Integer id) {
return wechatUserServiceImpl.selectByPrimaryKey(id);
}

}
