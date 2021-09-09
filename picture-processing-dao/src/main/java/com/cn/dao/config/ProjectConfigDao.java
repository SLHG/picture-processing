package com.cn.dao.config;

import com.cn.beans.config.ProjectConfigBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectConfigDao {

    List<ProjectConfigBean> getProjectConfig();

    List<ProjectConfigBean> getList();

    int del(@Param("configKey") String projectConfigKey);

    ProjectConfigBean get(@Param("configKey") String configKey);

    int save(ProjectConfigBean projectConfig);

    int update(ProjectConfigBean projectConfig);
}
