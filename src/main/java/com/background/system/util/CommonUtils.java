package com.background.system.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 镜像
 * @create 2023/3/1 21:53
 */
public class CommonUtils {

    public static List<String> getTargets(String source){
        if (StringUtils.isBlank(source)){
            return Collections.emptyList();
        }
        return Arrays.stream(source.split(",")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }
}
