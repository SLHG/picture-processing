package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.beans.config.ProjectConfigBean;
import com.cn.config.ConfigReload;
import com.cn.service.config.ProjectConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manager/config")
@Scope("prototype")
@Slf4j
public class ProjectConfigManagerController {

    @Autowired
    ProjectConfigService projectConfigService;

    @GetMapping("/get")
    public ResultBean getConfigList() {
        return new ResultBean(projectConfigService.getList());
    }

    @DeleteMapping("/delete")
    public ResultBean del(String configKey) {
        int num = projectConfigService.del(configKey);
        if (num > 0) {
            ConfigReload.reloadConfigList();
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }

    @PostMapping("/save")
    public ResultBean save(@RequestBody ProjectConfigBean projectConfig) {
        int num = projectConfigService.save(projectConfig);
        if (num > 0) {
            ConfigReload.reloadConfigList();
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }
}
