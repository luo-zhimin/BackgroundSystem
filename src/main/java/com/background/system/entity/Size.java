package com.background.system.entity;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/20 14:53
*/
@ApiModel(value="产品")
@Data
@ToString
public class Size {
    @ApiModelProperty(value="主键id")
    private String id;

    @ApiModelProperty(value="标题")
    private String title;

    @ApiModelProperty(value="尺寸")
    private String name;

    @ApiModelProperty(value="产品详情页图 多个逗号分隔")
    private String pic;

    @ApiModelProperty(value="尺寸的大图 多个逗号分隔")
    private String detailPic;

    @ApiModelProperty(value="原价")
    private BigDecimal price;

    @ApiModelProperty(value="优惠后价格")
    private BigDecimal uPrice;

    @ApiModelProperty(value = "材质id集合")
    private String materialId;

    @ApiModelProperty(value = "尺寸 第一个width 第二个height")
    private String size;

    @ApiModelProperty(value = "单面 or 双面")
    private String faces;

    private Boolean isDel;

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

    public List<String> getDetailPictureIds() {
        if (StringUtils.isNotBlank(this.detailPic)) {
            return Lists.newArrayList(this.detailPic.split(","));
        }
        return Collections.emptyList();
    }
}