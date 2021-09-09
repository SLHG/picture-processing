package com.cn.service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;

@Component
@Slf4j
public class ProjectConfig extends ConfigReload implements ApplicationRunner {

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
        log.info("{}", properties);
        log.info("开始加载项目配置");
        PROJECT_CONFIG = projectConfigService.getProjectConfig();
        log.info("项目配置加载完成:{}", PROJECT_CONFIG);
    }
}
