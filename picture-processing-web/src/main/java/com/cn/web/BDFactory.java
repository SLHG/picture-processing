package com.cn.web;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.cn.service.config.ConfigReload;
import com.cn.service.config.ProjectConfig;

public class BDFactory extends ConfigReload {
    private static AipBodyAnalysis aipBodyAnalysis;

    public static AipBodyAnalysis getAipBodyAnalysis() {
        if (aipBodyAnalysis == null) {
            synchronized (AipBodyAnalysis.class) {
                if (aipBodyAnalysis == null) {
                    RELOAD_LIST.add(new BDFactory());
                    load();
                }
            }
        }
        return aipBodyAnalysis;
    }

    static void load() {
        aipBodyAnalysis = new AipBodyAnalysis(ProjectConfig.PROJECT_CONFIG.get("baidu_appid"), ProjectConfig.PROJECT_CONFIG.get("baidu_api_key"), ProjectConfig.PROJECT_CONFIG.get("baidu_secret_key"));
    }

    @Override
    public void reload() {
        load();
    }
}
