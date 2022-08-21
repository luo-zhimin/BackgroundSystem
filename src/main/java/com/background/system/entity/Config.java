package com.background.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Config {

    @ApiModelProperty(value="主键id")
    private Long id;

    @ApiModelProperty(value="配置key")
    private String configKey;

    @ApiModelProperty(value="配置value")
    private String configValue;

    @ApiModelProperty(value="备注")
    private String remark;
}