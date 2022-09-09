package com.background.system.service;

import com.background.system.entity.Picture;
import com.background.system.response.PictureResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 10:10
 */
public interface PictureService {

    PictureResponse getPicture(MultipartFile file,String father);

    Picture getIndexPicture();

    Boolean updateIndexPicture(PictureResponse request);

    List<PictureResponse> upload(MultipartFile[] file,String father);
}
