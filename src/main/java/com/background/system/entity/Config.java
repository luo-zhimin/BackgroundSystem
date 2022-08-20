package com.background.system.entity;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Config {

    private Long id;

    private String configKey;

    private String configValue;

    private String remark;
}