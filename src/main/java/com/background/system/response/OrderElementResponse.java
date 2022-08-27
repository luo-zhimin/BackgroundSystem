package com.background.system.response;

import com.background.system.entity.OrderElement;
import com.background.system.entity.Picture;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/27 14:23
 */
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
public class OrderElementResponse extends OrderElement {

    private List<Picture> pictures;
}
