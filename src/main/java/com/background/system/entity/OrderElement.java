package com.background.system.entity;

import com.background.system.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2022/8/27 12:40
 */
@Data
@ToString
public class OrderElement implements Serializable {

    private static final long serialVersionUID = 6540414636392908109L;

    private Long id;

    @ApiModelProperty(value = "订单号")
    @Excel(name = "订单号")
    private String orderId;

    @ApiModelProperty(value = "'商品数量'")
    @Excel(name = "商品数量")
    private Integer number;

    @ApiModelProperty(value = "'商品图片'")
    @Excel(name = "商品图片编号")
    private String pictureId;

    @ApiModelProperty(value = "'是否删除'")
    private Boolean isDel;

    @ApiModelProperty(value = "'创建时间'")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    public List<String> getPictureIds() {
        if (StringUtils.isNotBlank(this.pictureId)) {
            return Lists.newArrayList(this.pictureId.split(","));
        }
        return Collections.emptyList();
    }
}
