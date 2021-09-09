package com.cn.beans.config;

public class ProjectConfigBean {

    //配置名称
    private String configName;
    //配置key
    private String configKey;
    //配置值
    private String configValue;
    //配置描述
    private String configDesc;
    //创建时间
    private String createTime;

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigDesc() {
        return configDesc;
    }

    public void setConfigDesc(String configDesc) {
        this.configDesc = configDesc;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "ProjectConfigBean{" +
                "configName='" + configName + '\'' +
                ", configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                ", configDesc='" + configDesc + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
