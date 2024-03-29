package com.background.system.response.file;

import com.background.system.entity.Picture;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/9/3 03:39
 */
@Data
@Builder
public class ReadyDownloadFileResponse {

    @ApiModelProperty(value = "order id")
    private String id;

    private String sizeName;

    private String wxNo;

    private String pictureId;

    //todo 图片 尺寸 转换 zip 待定
    @ApiModelProperty(value = "尺寸 第一个width 第二个height")
    private String size;//mm*mm

    private List<Picture> pictures = Lists.newArrayList();

    private Integer number;

    private Integer onlyNumber;

    private Map<String,List<Picture>> pictureMap;

    private String face;

    @Tolerate
    public ReadyDownloadFileResponse(){}

//    public List<String> getPictureIds() {
//        if (StringUtils.isNotBlank(this.pictureId)) {
//            return Lists.newArrayList(this.pictureId.split(","));
//        }
//        return Collections.emptyList();
//    }

    public List<String> getPictureIds() {
        if (StringUtils.isNotBlank(this.pictureId) && this.onlyNumber!=null) {
            StringBuilder picture = new StringBuilder();
            for (int i = 0; i < this.onlyNumber; i++) {
                picture.append(this.pictureId).append(",");
            }
            return Lists.newArrayList(picture.substring(0,picture.length()-1).split(","));
        }
        return Collections.emptyList();
    }

    public int getWeight(){
        if (StringUtils.isNotEmpty(this.size)){
            return Integer.parseInt(size.split("\\*")[0]);
        }
        return 0;
    }

    public int getHeight(){
        if (StringUtils.isNotEmpty(this.size)){
            return Integer.parseInt(size.split("\\*")[1]);
        }
        return 0;
    }
}
