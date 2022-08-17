package com.background.system.mapper.admin;

import com.background.system.entity.vo.AdminUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminUserMapper {

    /**
     * 查询管理后台用户
     *
     * @param userName
     * @param password
     * @return
     */
    Boolean queryUser(@Param("userName") String userName, @Param("password") String password);

    /**
     * 获取唯一的管理员账号信息
     *
     * @return
     */
    AdminUserVO getAdminInfo();

}
