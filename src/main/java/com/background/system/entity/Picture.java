package com.background.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
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
@Builder
public class Picture {

    @ApiModelProperty(value="主键Id")
    private String id;

    @ApiModelProperty(value="文件名字")
    private String name;

    @ApiModelProperty(value="图片地址")
    private String url;

    @ApiModelProperty(value="是否删除")
    private Boolean isDel;

    @ApiModelProperty(value = "父级目录")
    private String father;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @Tolerate
    public Picture(){}
}