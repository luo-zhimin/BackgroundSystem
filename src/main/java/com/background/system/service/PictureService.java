package com.background.system.service;

import com.background.system.response.PictureResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 10:10
 */
public interface PictureService {

    PictureResponse getPicture(MultipartFile file);
}
