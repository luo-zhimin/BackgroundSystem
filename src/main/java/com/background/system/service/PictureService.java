package com.background.system.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 10:10
 */
public interface PictureService {

    String getPicture(List<MultipartFile> file);
}
