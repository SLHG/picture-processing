package com.cn.service.impl.manger;

import com.cn.beans.common.Constant;
import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPicturePendantInfo;
import com.cn.dao.manager.ManagerPicturePendantDao;
import com.cn.service.config.ProjectConfig;
import com.cn.service.manger.ManagerPicturePendantService;
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
public class ManagerPicturePendantServiceImpl implements ManagerPicturePendantService {

    final
    ManagerPicturePendantDao managerPicturePendantDao;


    public ManagerPicturePendantServiceImpl(ManagerPicturePendantDao managerPicturePendantDao) {
        this.managerPicturePendantDao = managerPicturePendantDao;
    }

    @Override
    public PageInfo<ManagerPicturePendantInfo> getList(int start, int page) {
        PageHelper.startPage(start, page);
        return new PageInfo<>(managerPicturePendantDao.getList());
    }

    @Override
    public ResultBean uploadFile(MultipartFile file, String pendantName) {
        String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.PENDANT_UPLOAD_BASE_DIR.getValue(String.class));
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
        ManagerPicturePendantInfo info = new ManagerPicturePendantInfo();
        info.setPicturePath(filePathName);
        info.setPendantName(pendantName);
        int i = managerPicturePendantDao.insert(info);
        if (i > 0) {
            return new ResultBean();
        } else {
            FileUtils.delFile(baseDir, filePathName);
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
    }

    @Override
    public ResultBean delete(String id) {
        ManagerPicturePendantInfo info = managerPicturePendantDao.selectById(id);
        if (info == null) {
            return new ResultBean(ResultBean.FAIL_CODE, "数据不存在");
        }
        if (!StringUtils.isBlank(info.getPicturePath())) {
            String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.PENDANT_UPLOAD_BASE_DIR.getValue(String.class));
            boolean delFile = FileUtils.delFile(baseDir, info.getPicturePath());
            if (!delFile) {
                return new ResultBean(ResultBean.FAIL_CODE, "删除失败");
            }
        }
        int i = managerPicturePendantDao.delete(id);
        if (i > 0) {
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }


}
