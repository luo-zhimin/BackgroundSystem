package com.background.system.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Config implements Serializable {

    private static final long serialVersionUID = 1967784472744837312L;

    @ApiModelProperty(value="主键id")
    private Long id;

    @ApiModelProperty(value="配置key")
    private String configKey;

    @ApiModelProperty(value="配置value")
    private String configValue;

    @ApiModelProperty(value="备注")
    private String remark;
}