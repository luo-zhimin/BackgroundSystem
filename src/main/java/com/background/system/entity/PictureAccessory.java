package com.background.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@ApiModel(value="picture_accessory")
@Data
@AllArgsConstructor
public class PictureAccessory {

    @ApiModelProperty(value="主键id")
    private Long id;

    @ApiModelProperty(value="图片id")
    private Long pictureId;

    @ApiModelProperty(value="点赞数")
    private Long like;

    @ApiModelProperty(value="下单数")
    private Long num;

    @ApiModelProperty(value="访问量")
    private Long pv;
}