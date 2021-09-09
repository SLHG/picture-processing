package com.cn.beans.config;

import lombok.Data;

@Data
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

}
