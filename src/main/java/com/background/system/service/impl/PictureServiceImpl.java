package com.background.system.service.impl;

import com.background.system.entity.Picture;
import com.background.system.mapper.PictureMapper;
import com.background.system.service.PictureService;
import com.background.system.util.AliUploadUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@Service
public class PictureServiceImpl implements PictureService {

    @Resource
    private PictureMapper pictureMapper;

    @Override
    @Transactional
    public String getPicture(MultipartFile file) {
        String aDefault = AliUploadUtils.uploadImage(file, "default");
        Picture picture = new Picture();
        picture.setUrl(aDefault);
        picture.setIsDel(false);
        picture.setFather("default");
        picture.setName(file.getName());
        picture.setCreateTime(LocalDateTime.now());
        pictureMapper.insert(picture);
        return picture.getId();
    }

    public List<Picture> getPicturesByIds(List<String> ids){
        if (CollectionUtils.isNotEmpty(ids)){
            return this.pictureMapper.getPicturesByIds(ids);
        }
        return Collections.emptyList();
    }
}
