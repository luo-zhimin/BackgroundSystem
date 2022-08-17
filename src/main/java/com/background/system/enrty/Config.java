package com.background.system.enrty;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/17 16:07
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class Config extends BaseEntry{

    private Long id;

    private String configKey;

    private String configValue;

    private String remark;
}
