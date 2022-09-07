package com.background.system.response;

import com.background.system.entity.Caizhi;
import com.background.system.entity.Picture;
import com.background.system.entity.Size;
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
public class SizeResponse extends Size {

    @ApiModelProperty(value = "图片集合")
    private List<Picture> pictures;

    private List<Picture> detailPictures;

    @ApiModelProperty(value = "材质集合")
    private List<Caizhi> materials;
}
