package com.background.system.response;

import com.background.system.entity.Coupon;
import com.background.system.entity.Picture;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/22 18:11
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class CouponResponse extends Coupon {

    private Picture picture;
}
