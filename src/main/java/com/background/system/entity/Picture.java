package com.background.system.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@ApiModel(value="picture")
@Data
@AllArgsConstructor
public class Picture {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="图片名称")
    private String title;

    @ApiModelProperty(value="图片地址")
    private String url;

    @ApiModelProperty(value="置顶")
    private String top;

    @ApiModelProperty(value="上级目录")
    private String father;

    @ApiModelProperty(value="是否删除")
    private String isDel;

    @ApiModelProperty(value="创建时间")
    private Date createTime;
}