package com.background.system.service;

import com.background.system.entity.Picture;
import com.background.system.response.PictureResponse;
import com.background.system.response.file.UploadZipFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * @Author : 镜像
 * @create 2022/8/23 10:10
 */
public interface PictureService {

    PictureResponse getPicture(MultipartFile file,String father);

    Picture getIndexPicture();

    Boolean updateIndexPicture(PictureResponse request);

    List<PictureResponse> upload(List<UploadZipFileResponse> uploadFiles, String father);

    Map<String,Object> readMemory();
}
