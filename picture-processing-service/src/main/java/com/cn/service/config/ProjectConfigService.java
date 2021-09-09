package com.cn.service.config;

import com.cn.beans.config.ProjectConfigBean;

import java.util.List;
import java.util.Map;

public interface ProjectConfigService {
    Map<String, String> getProjectConfig();

    List<ProjectConfigBean> getList();

    int del(String configKey);

    int save(ProjectConfigBean projectConfig);
}
