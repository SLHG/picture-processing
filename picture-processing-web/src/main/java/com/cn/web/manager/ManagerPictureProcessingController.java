package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.service.manger.ManagerPictureProcessingService;
import com.cn.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/manager/picture")
@Slf4j
public class ManagerPictureProcessingController {

    final
    ManagerPictureProcessingService managerPictureProcessingService;

    public ManagerPictureProcessingController(ManagerPictureProcessingService managerPictureProcessingService) {
        this.managerPictureProcessingService = managerPictureProcessingService;
    }

    @GetMapping("/get")
    public ResultBean getList(@RequestParam(defaultValue = "1") int start, @RequestParam(defaultValue = "10") int page, String id) {
        if (SecurityUtils.isAdmin(SecurityContextHolder.getContext())) {
            return new ResultBean(managerPictureProcessingService.getList(start, page, id));
        }
        if (StringUtils.isBlank(id)) {
            return new ResultBean();
        }
        return new ResultBean(managerPictureProcessingService.getList(start, page, id));
    }

}
