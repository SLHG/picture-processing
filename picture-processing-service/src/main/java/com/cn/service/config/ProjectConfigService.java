package com.cn.service.config;

import com.cn.beans.config.ProjectConfigBean;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface ProjectConfigService {
    Map<String, String> getProjectConfig();

    PageInfo<ProjectConfigBean> getList(int start, int page);

    int del(String configKey);

    int save(ProjectConfigBean projectConfig);
}
