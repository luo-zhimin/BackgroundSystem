package com.background.system.response;

import com.background.system.entity.Goods;
import com.background.system.entity.Picture;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 志敏.罗
 * @create 2022/8/19 22:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class GoodsResponse extends Goods {

    @ApiModelProperty(value = "图片集合")
    private List<Picture> pictures;
}
