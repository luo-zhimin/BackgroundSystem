package com.background.system.service.impl;

import com.background.system.entity.Picture;
import com.background.system.exception.ServiceException;
import com.background.system.mapper.PictureMapper;
import com.background.system.response.PictureResponse;
import com.background.system.service.PictureService;
import com.background.system.util.AliUploadUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
* Created by IntelliJ IDEA.
* @Author : 志敏.罗
* @create 2022/8/19 22:03
*/
@Service
public class PictureServiceImpl implements PictureService {

    private static Logger logger = LoggerFactory.getLogger(PictureServiceImpl.class);

    @Resource
    private PictureMapper pictureMapper;

    @Override
    @Transactional
    public PictureResponse getPicture(MultipartFile file,String father) {
        if (file == null) {
            throw new ServiceException(1000, "请至少选择一张图片！");
        }

        String aDefault = AliUploadUtils.uploadImage(file, father);
        Picture picture = new Picture();
        picture.setUrl(aDefault);
        picture.setIsDel(false);
        picture.setFather(father);
        picture.setName(file.getOriginalFilename());
        picture.setCreateTime(LocalDateTime.now());
        pictureMapper.insert(picture);
        return PictureResponse.builder()
                .id(picture.getId())
                .url(picture.getUrl())
                .build();
    }

    @Override
    public Picture getIndexPicture() {
        return Optional.ofNullable(pictureMapper.getIndexPicture()).orElse(new Picture());
    }

    @Override
    public Boolean updateIndexPicture(PictureResponse request) {
        logger.info("logger request[{}]",request);
        if (StringUtils.isEmpty(request.getUrl())){
            throw new ServiceException(1000,"请填写要修改的图片地址");
        }
        return pictureMapper.updateIndexPicture(request.getUrl(),request.getId())>0;
    }

    @Override
    public List<PictureResponse> upload(MultipartFile[] files,String father) {
        if (files==null || files.length<=0){
            throw new ServiceException(1000, "请至少选择一张图片！");
        }
        Map<String, String> pictureMap = AliUploadUtils.uploadImages(files, "father");
        List<Picture> pictures = Lists.newArrayList();
        pictureMap.forEach((k,v)->{
            Picture picture = new Picture();
            picture.setUrl(v);
            picture.setFather(father);
            picture.setName(k);
            pictures.add(picture);
        });
        pictureMapper.batchInsert(pictures);
        List<PictureResponse> responses = Lists.newArrayList();
        pictures.forEach(picture -> {
            responses.add(PictureResponse.builder()
                    .id(picture.getId())
                    .url(picture.getUrl())
                    .build());
        });
        return responses;
    }

    public List<Picture> getPicturesByIds(List<String> ids){
        if (CollectionUtils.isNotEmpty(ids)){
            return this.pictureMapper.getPicturesByIds(ids);
        }
        return Collections.emptyList();
    }
}
