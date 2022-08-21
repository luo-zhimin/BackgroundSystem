package com.background.system.request;

import com.background.system.entity.Address;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by IntelliJ IDEA.
 * @Author : 志敏.罗
 * @create 2022/8/20 12:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressRequest extends Address {

    @ApiModelProperty(value="页码")
    private Integer page = 0;

    @ApiModelProperty(value="条数")
    private Integer size = 10;
}
