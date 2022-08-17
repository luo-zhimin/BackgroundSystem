package com.background.system.enrty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/17 16:08
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class WechatUser extends BaseEntry{

    private String openId;

    private String unionId;

    private  String userInfo;

    private Boolean isDel;

    private Date createTime;
}
