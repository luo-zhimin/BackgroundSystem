package com.background.system.entity;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@ApiModel(value="`size`")
@Data
public class Size {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="尺寸")
    private String name;

    @ApiModelProperty(value="尺寸详情页的大图")
    private String pic;

    @ApiModelProperty(value="原价")
    private BigDecimal price;

    @ApiModelProperty(value="优惠后价格")
    private BigDecimal uPrice;

    public List<String> getPictureIds() {
        if (StringUtils.isNotBlank(this.pic)) {
            return Lists.newArrayList(this.pic.split(","));
        }
        return Collections.emptyList();
    }
}