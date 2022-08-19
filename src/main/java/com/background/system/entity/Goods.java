package com.background.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 21:17
 */
@ApiModel(value = "goods")
@Data
@Builder
@AllArgsConstructor
public class Goods {

    @ApiModelProperty(value = "主键Id")
    private Long id;

    @ApiModelProperty(value = "图片id")
    private String pictureId;

    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    @ApiModelProperty(value = "材质")
    private String materialQuality;

    @ApiModelProperty(value = "款式")
    private String style;

    @ApiModelProperty(value = "'优惠价'")
    private BigDecimal disCount;

    @ApiModelProperty(value = "'是否删除'")
    private Boolean isDel;

    @ApiModelProperty(value = "'创建时间'")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime createTime;

    @ApiModelProperty(value = "'修改时间'")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    private LocalDateTime updateTime;

    public List<String> getPictureIds() {
        if (StringUtils.isNotBlank(this.pictureId)) {
            return Lists.newArrayList(this.pictureId.split(","));
        }
        return Collections.emptyList();
    }

    @Tolerate
    public Goods() {
    }
}