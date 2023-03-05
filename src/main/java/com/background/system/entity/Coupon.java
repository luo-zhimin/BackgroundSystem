package com.background.system.entity;

import com.background.system.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/8/20 3:31 PM
 */
@Data
//@Builder
//@AllArgsConstructor
@ToString
public class Coupon implements Serializable {

    private static final long serialVersionUID = 8449488559542598451L;

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "兑换码")
    @Excel(name = "兑换码")
    private String couponId;

    @ApiModelProperty(value = "是否被使用")
    @Excel(name = "是否被使用",readConverterExp = "false=未使用,true=已使用")
    private Boolean isUsed;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "消费限制")
    @Excel(name = "消费限制")
    private Integer useLimit;

    @ApiModelProperty(value = "小程序唯一标识")
    private String openId;

    @ApiModelProperty(value = "'是否失效'")
    @Excel(name = "是否失效",readConverterExp = "false=未失效,true=已失效")
    private Boolean status;

    private String pictureId;

    @ApiModelProperty(value = "发布时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime releaseTime;
}
