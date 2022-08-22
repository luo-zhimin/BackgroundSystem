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
@ApiModel(value="尺寸")
@Data
public class Size {
    @ApiModelProperty(value="主键id")
    private String id;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="尺寸")
    private String name;

    @ApiModelProperty(value="尺寸详情页的大图 多个逗号分隔")
    private String pic;

    @ApiModelProperty(value="原价")
    private BigDecimal price;

    @ApiModelProperty(value="优惠后价格")
    private BigDecimal uPrice;

    @ApiModelProperty(value = "材质id集合")
    private String materialId;

    public List<String> getPictureIds() {
        if (StringUtils.isNotBlank(this.pic)) {
            return Lists.newArrayList(this.pic.split(","));
        }
        return Collections.emptyList();
    }

    public List<String> getMaterialIds(){
        if (StringUtils.isNotBlank(this.materialId)) {
            return Lists.newArrayList(this.materialId.split(","));
        }
        return Collections.emptyList();
    }
}