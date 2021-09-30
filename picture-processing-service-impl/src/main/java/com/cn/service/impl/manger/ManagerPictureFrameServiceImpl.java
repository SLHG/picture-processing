package com.cn.service.impl.manger;

import com.cn.beans.common.Constant;
import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureFrameInfo;
import com.cn.dao.manager.ManagerPictureFrameDao;
import com.cn.service.config.ProjectConfig;
import com.cn.service.manger.ManagerPictureFrameService;
import com.cn.utils.FileSizeLimitExceededException;
import com.cn.utils.FileUtils;
import com.cn.utils.InvalidExtensionException;
import com.cn.utils.MimeTypeUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ManagerPictureFrameServiceImpl implements ManagerPictureFrameService {

    final
    ManagerPictureFrameDao managerPictureFrameDao;


    public ManagerPictureFrameServiceImpl(ManagerPictureFrameDao managerPictureFrameDao) {
        this.managerPictureFrameDao = managerPictureFrameDao;
    }

    @Override
    public PageInfo<ManagerPictureFrameInfo> getList(int start, int page) {
        PageHelper.startPage(start, page);
        return new PageInfo<>(managerPictureFrameDao.getList());
    }

    @Override
    public ResultBean uploadFile(MultipartFile file, String frameName) {
        String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.FRAME_UPLOAD_BASE_DIR);
        if (StringUtils.isBlank(baseDir)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请设置文件上传基础目录");
        }
        String filePathName;
        try {
            filePathName = FileUtils.upload(baseDir, file, MimeTypeUtils.PNG_JPG_EXTENSION);
        } catch (FileSizeLimitExceededException e) {
            log.error("uploadFile=>文件大小超出最大限制", e);
            return new ResultBean(ResultBean.FAIL_CODE, "文件大小超出最大限制");
        } catch (InvalidExtensionException e) {
            log.error("uploadFile=>文件格式错误", e);
            return new ResultBean(ResultBean.FAIL_CODE, "文件格式错误");
        } catch (IOException e) {
            log.error("uploadFile=>上传失败", e);
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
        if (StringUtils.isBlank(filePathName)) {
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
        ManagerPictureFrameInfo info = new ManagerPictureFrameInfo();
        info.setPicturePath(filePathName);
        info.setFrameName(frameName);
        int i = managerPictureFrameDao.insert(info);
        if (i > 0) {
            return new ResultBean();
        } else {
            FileUtils.delFile(baseDir, filePathName);
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
    }

    @Override
    public ResultBean delete(String id) {
        int i = managerPictureFrameDao.delete(id);
        if (i > 0) {
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }


}
