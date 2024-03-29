package com.background.system.service;

import com.background.system.entity.Config;
import com.background.system.request.ConfigRequest;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ConfigService{

    Config selectByPrimaryKey(Long id);

    Page<Config> getConfigList(Integer page, Integer size);

    Boolean addConfig(ConfigRequest request);

    Boolean updateConfig(ConfigRequest request);

    List<Config> selectConfigList(Config config);
}
