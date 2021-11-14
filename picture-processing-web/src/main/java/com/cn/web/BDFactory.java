package com.cn.web;

import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.cn.beans.common.Constant;
import com.cn.service.config.ConfigReload;
import com.cn.service.config.ProjectConfig;

public class BDFactory extends ConfigReload {
    private static volatile AipBodyAnalysis aipBodyAnalysis;

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
        aipBodyAnalysis = new AipBodyAnalysis(ProjectConfig.PROJECT_CONFIG.get(Constant.BAIDU_APPID.getValue(String.class))
                , ProjectConfig.PROJECT_CONFIG.get(Constant.BAIDU_API_KEY.getValue(String.class))
                , ProjectConfig.PROJECT_CONFIG.get(Constant.BAIDU_SECRET_KEY.getValue(String.class)));
    }

    @Override
    public void reload() {
        load();
    }
}
