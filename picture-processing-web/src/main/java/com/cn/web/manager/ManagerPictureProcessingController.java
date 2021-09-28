package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.cn.service.config.ProjectConfig;
import com.cn.service.manger.ManagerPictureProcessingService;
import com.cn.utils.SecurityUtils;
import com.github.pagehelper.PageInfo;
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

    private static final String IMAGE_BASE_URL = "image_base_url";

    public ManagerPictureProcessingController(ManagerPictureProcessingService managerPictureProcessingService) {
        this.managerPictureProcessingService = managerPictureProcessingService;
    }

    @GetMapping("/get")
    public ResultBean getList(@RequestParam(defaultValue = "1") int start, @RequestParam(defaultValue = "10") int page, String id) {
        if (SecurityUtils.isAdmin(SecurityContextHolder.getContext())) {
            return getResultList(start, page, id);
        }
        //非管理员只能根据id进行数据查询,不展示数据列表
        if (StringUtils.isBlank(id)) {
            return new ResultBean();
        }
        return getResultList(start, page, id);
    }

    private ResultBean getResultList(int start, int page, String id) {
        PageInfo<ManagerPictureProcessingInfo> list = managerPictureProcessingService.getList(start, page, id);
        String baseUrl = ProjectConfig.PROJECT_CONFIG.get(IMAGE_BASE_URL);
        ResultBean result = new ResultBean();
        result.setPageInfo(list);
        result.setResult(baseUrl);
        return result;
    }

}
