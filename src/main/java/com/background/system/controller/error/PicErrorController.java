package com.background.system.controller.error;

import cn.hutool.http.HttpRequest;
import com.background.system.annotation.IgnoreLogin;
import com.background.system.entity.Picture;
import com.background.system.mapper.PictureMapper;
import com.background.system.util.Result;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: 方糖
 * @Date: 2022/11/17 12:06 PM
 */
@Api(tags = "纠错控制")
@RestController
@RequestMapping("error")
public class PicErrorController {

    @Resource
    private PictureMapper pictureMapper;

    @IgnoreLogin
    @GetMapping("getPicErrorList")
    @ApiOperation("图片纠错")
    public Result<?> getPicErrorList (String num) {
        List<Picture> pictures = pictureMapper.selectList(
                new LambdaQueryWrapper<Picture>().orderByDesc(Picture::getCreateTime).last("limit " + num));
        List<Picture> errorList = new ArrayList<>();
        pictures.forEach(picture -> {
            try {
                String t = "http://119.23.228.135:8600/hello?url=" + picture.getUrl() + "&type=0&os=0&xnx=0";
                HttpRequest.get(t).execute().body();
            }catch (Exception e) {
                errorList.add(picture);
            }
        });
        if (errorList.size() == 0) return Result.success("无异常图片");
        return Result.success(errorList);
    }
}
