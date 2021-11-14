package com.cn.service.impl.wx;

import cn.hutool.core.util.IdUtil;
import com.cn.beans.common.Constant;
import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureFrameInfo;
import com.cn.beans.manager.ManagerPicturePendantInfo;
import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.cn.dao.manager.ManagerPictureFrameDao;
import com.cn.dao.manager.ManagerPicturePendantDao;
import com.cn.dao.manager.ManagerPictureProcessingDao;
import com.cn.service.config.ProjectConfig;
import com.cn.service.wx.PictureProcessingService;
import com.cn.utils.FileSizeLimitExceededException;
import com.cn.utils.FileUtils;
import com.cn.utils.InvalidExtensionException;
import com.cn.utils.MimeTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PictureProcessingServiceImpl implements PictureProcessingService {

    final
    ManagerPictureProcessingDao managerPictureProcessingDao;
    final
    ManagerPictureFrameDao managerPictureFrameDao;
    final
    ManagerPicturePendantDao managerPicturePendantDao;

    public PictureProcessingServiceImpl(ManagerPictureProcessingDao managerPictureProcessingDao, ManagerPictureFrameDao managerPictureFrameDao, ManagerPicturePendantDao managerPicturePendantDao) {
        this.managerPictureProcessingDao = managerPictureProcessingDao;
        this.managerPictureFrameDao = managerPictureFrameDao;
        this.managerPicturePendantDao = managerPicturePendantDao;
    }

    @Override
    public ResultBean uploadFile(MultipartFile file, String openId) {
        String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.PHOTO_UPLOAD_BASE_DIR.getValue(String.class));
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
        ManagerPictureProcessingInfo info = new ManagerPictureProcessingInfo();
        info.setOpenId(openId);
        info.setPicturePath(filePathName);
        String id = IdUtil.fastSimpleUUID();
        info.setId(id);
        int i = managerPictureProcessingDao.insert(info);
        if (i > 0) {
            return new ResultBean(id);
        } else {
            FileUtils.delFile(baseDir, filePathName);
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
    }

    @Override
    public ResultBean getFrameList() {
        String baseUrl = ProjectConfig.PROJECT_CONFIG.get(Constant.IFRAME_BASE_URL.getValue(String.class));
        List<ManagerPictureFrameInfo> list = managerPictureFrameDao.getList();
        ResultBean resultBean = new ResultBean();
        resultBean.setResult(baseUrl);
        resultBean.setResultList(list);
        return resultBean;
    }

    @Override
    public ResultBean getPendantList() {
        String baseUrl = ProjectConfig.PROJECT_CONFIG.get(Constant.PENDANT_BASE_URL.getValue(String.class));
        List<ManagerPicturePendantInfo> list = managerPicturePendantDao.getList();
        ResultBean resultBean = new ResultBean();
        resultBean.setResult(baseUrl);
        resultBean.setResultList(list);
        return resultBean;
    }
}
