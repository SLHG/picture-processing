package com.cn.config;

import java.util.ArrayList;
import java.util.List;

public abstract class ConfigReload {


    public static List<ConfigReload> RELOAD_LIST = new ArrayList<>();

    //重加载
   public abstract void reload();

    //重新加载项目配置
    public static void reloadConfigList() {
        for (ConfigReload configReload : RELOAD_LIST) {
            configReload.reload();
        }
    }
}
