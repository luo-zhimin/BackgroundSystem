package com.background.system.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/18 21:58
 */
@Data
@ToString
public class WechatUserInfo {

    @ApiModelProperty(value="小程序唯一标识")
    private String openId;

    @ApiModelProperty(value="用户信息")
    private String userInfo;
}
