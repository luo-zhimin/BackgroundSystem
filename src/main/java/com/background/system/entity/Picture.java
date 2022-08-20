package com.background.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@ApiModel(value="picture")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Picture {
    @ApiModelProperty(value="")
    private Long id;

    @ApiModelProperty(value="图片地址")
    private String url;

    @ApiModelProperty(value="是否删除")
    private String isDel;

}