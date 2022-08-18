package com.background.system.request;

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

    private String openId;

    private String userInfo;
}
