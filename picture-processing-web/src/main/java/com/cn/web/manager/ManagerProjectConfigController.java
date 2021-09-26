package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.beans.config.ProjectConfigBean;
import com.cn.service.config.ConfigReload;
import com.cn.service.config.ProjectConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manager/config")
@Slf4j
public class ManagerProjectConfigController {

    final
    ProjectConfigService projectConfigService;

    public ManagerProjectConfigController(ProjectConfigService projectConfigService) {
        this.projectConfigService = projectConfigService;
    }

    @GetMapping("/get")
    public ResultBean getConfigList(@RequestParam(defaultValue = "1") int start, @RequestParam(defaultValue = "10") int page) {
        return new ResultBean(projectConfigService.getList(start, page));
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
