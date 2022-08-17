package com.background.system.service.admin;

import com.background.system.entity.vo.AdminUserVO;

public interface IAdminUseService {

    /**
     * 查询管理后台用户
     *
     * @param userName
     * @param password
     * @return
     */
    Boolean queryUser(String userName, String password);

    /**
     * 获取唯一的管理员账号信息
     *
     * @return
     */
    AdminUserVO getAdminInfo();

}
