package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.cn.service.config.ProjectConfig;
import com.cn.service.manger.ManagerPictureProcessingService;
import com.cn.utils.FileUtils;
import com.cn.utils.MimeTypeUtils;
import com.cn.utils.SecurityUtils;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
            return getResultListLikeId(start, page, id);
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

    private ResultBean getResultListLikeId(int start, int page, String id) {
        //根据id模糊查询
        PageInfo<ManagerPictureProcessingInfo> list = managerPictureProcessingService.getListLikeId(start, page, id);
        String baseUrl = ProjectConfig.PROJECT_CONFIG.get(IMAGE_BASE_URL);
        ResultBean result = new ResultBean();
        result.setPageInfo(list);
        result.setResult(baseUrl);
        return result;
    }

    @PostMapping("/download")
    public void download(@RequestParam MultipartFile file, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setHeader("content-type", "text/html");
        if (file == null) {
            response.getWriter().write(new ResultBean(ResultBean.FAIL_CODE, "文件错误").toJSONString());
            return;
        }
        String extension = FileUtils.getExtension(file);
        if (!FileUtils.isAllowedExtension(extension, MimeTypeUtils.XLSX_EXTENSION)) {
            response.getWriter().write(new ResultBean(ResultBean.FAIL_CODE, "文件格式错误").toJSONString());
            return;
        }
        managerPictureProcessingService.download(file, response);
    }


}
