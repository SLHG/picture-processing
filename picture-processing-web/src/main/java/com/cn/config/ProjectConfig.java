package com.cn.config;

import com.cn.service.config.ProjectConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
public class ProjectConfig extends ConfigReload implements ApplicationRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectConfig.class);

    final
    ProjectConfigService projectConfigService;

    //项目配置
    public static Map<String, String> PROJECT_CONFIG = null;

    public ProjectConfig(ProjectConfigService projectConfigService) {
        this.projectConfigService = projectConfigService;
    }

    @Override
    public void run(ApplicationArguments args) {
        RELOAD_LIST.add(new ProjectConfig(projectConfigService));
        load();
    }

    @Override
    public void reload() {
        load();
    }

    void load() {
        Properties properties = System.getProperties();
        LOGGER.info("{}", properties);
        LOGGER.info("开始加载项目配置");
        PROJECT_CONFIG = projectConfigService.getProjectConfig();
        LOGGER.info("项目配置加载完成:{}", PROJECT_CONFIG);
    }
}
