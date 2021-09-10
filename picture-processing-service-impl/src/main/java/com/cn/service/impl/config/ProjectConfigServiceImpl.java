package com.cn.service.impl.config;

import com.cn.beans.config.ProjectConfigBean;
import com.cn.dao.config.ProjectConfigDao;
import com.cn.service.config.ProjectConfigService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectConfigServiceImpl implements ProjectConfigService {

    final
    ProjectConfigDao projectConfigDao;

    public ProjectConfigServiceImpl(ProjectConfigDao projectConfigDao) {
        this.projectConfigDao = projectConfigDao;
    }

    @Override
    public Map<String, String> getProjectConfig() {
        Map<String, String> map = new HashMap<>();
        List<ProjectConfigBean> list = projectConfigDao.getProjectConfig();
        for (ProjectConfigBean configBean : list) {
            map.put(configBean.getConfigKey(), configBean.getConfigValue());
        }
        return map;
    }

    @Override
    public List<ProjectConfigBean> getList() {
        return projectConfigDao.getList();
    }

    @Override
    public int del(String configKey) {
        return projectConfigDao.del(configKey);
    }

    @Override
    public int save(ProjectConfigBean projectConfig) {
        ProjectConfigBean config = projectConfigDao.get(projectConfig.getConfigKey());
        if (config == null) {
            return projectConfigDao.save(projectConfig);
        } else {
            return projectConfigDao.update(projectConfig);
        }
    }

}
