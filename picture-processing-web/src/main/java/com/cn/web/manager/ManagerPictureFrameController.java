package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureFrameInfo;
import com.cn.service.config.ProjectConfig;
import com.cn.service.manger.ManagerPictureFrameService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/manager/frame")
@Slf4j
public class ManagerPictureFrameController {

    final
    ManagerPictureFrameService managerPictureFrameService;

    private static final String IFRAME_BASE_URL = "iframe_base_url";

    /**
     * 默认大小 2M
     */
    public static final long DEFAULT_MAX_SIZE = 10 * 1024 * 1024;

    public ManagerPictureFrameController(ManagerPictureFrameService managerPictureFrameService) {
        this.managerPictureFrameService = managerPictureFrameService;
    }

    @GetMapping("/get")
    public ResultBean getList(@RequestParam(defaultValue = "1") int start, @RequestParam(defaultValue = "10") int page) {
        PageInfo<ManagerPictureFrameInfo> list = managerPictureFrameService.getList(start, page);
        String baseUrl = ProjectConfig.PROJECT_CONFIG.get(IFRAME_BASE_URL);
        ResultBean result = new ResultBean();
        result.setPageInfo(list);
        result.setResult(baseUrl);
        return result;
    }

    /**
     * 处理图片上传
     *
     * @param file 图片文件
     * @return 上传结果
     */
    @PostMapping(value = "/uploadFile", produces = "application/json;charset=UTF-8")
    public ResultBean uploadFile(@RequestParam(value = "file") MultipartFile file, @RequestParam String frameName) {
        ResultBean result = checkFile(file);
        if (result != null) {
            return result;
        }
        return managerPictureFrameService.uploadFile(file, frameName);
    }

    @DeleteMapping("/delete")
    public ResultBean delete(@RequestParam String id) {
        return managerPictureFrameService.delete(id);
    }

    private ResultBean checkFile(MultipartFile file) {
        if (file == null) {
            return new ResultBean(ResultBean.FAIL_CODE, "文件缺失");
        }
        if (file.getSize() > DEFAULT_MAX_SIZE) {
            return new ResultBean(ResultBean.FAIL_CODE, "图片超出最大可上传限制");
        }
        return null;
    }

}
