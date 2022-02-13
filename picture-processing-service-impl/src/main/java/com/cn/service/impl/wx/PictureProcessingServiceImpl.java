package com.cn.service.impl.wx;

import com.cn.beans.common.Constant;
import com.cn.beans.common.RedisKeyPrefix;
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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class PictureProcessingServiceImpl implements PictureProcessingService {

    final
    ManagerPictureProcessingDao managerPictureProcessingDao;
    final
    ManagerPictureFrameDao managerPictureFrameDao;
    final
    ManagerPicturePendantDao managerPicturePendantDao;
    final
    RedisTemplate<String, Object> redisTemplate;

    private static final String PICTURE_ID_PREFIX = "BZK";

    public PictureProcessingServiceImpl(ManagerPictureProcessingDao managerPictureProcessingDao, ManagerPictureFrameDao managerPictureFrameDao, ManagerPicturePendantDao managerPicturePendantDao, RedisTemplate<String, Object> redisTemplate) {
        this.managerPictureProcessingDao = managerPictureProcessingDao;
        this.managerPictureFrameDao = managerPictureFrameDao;
        this.managerPicturePendantDao = managerPicturePendantDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public ResultBean uploadFile(MultipartFile file, String openId, MultipartFile templateFile) {
        String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.PHOTO_UPLOAD_BASE_DIR.getValue(String.class));
        if (StringUtils.isBlank(baseDir)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请设置文件上传基础目录");
        }
        String filePathName;
        String templateFilePathName;
        try {
            filePathName = FileUtils.upload(baseDir, file, MimeTypeUtils.PNG_JPG_EXTENSION);
            templateFilePathName = FileUtils.upload(baseDir, templateFile, MimeTypeUtils.PNG_JPG_EXTENSION);
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
        if (StringUtils.isBlank(filePathName) || StringUtils.isBlank(templateFilePathName)) {
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
        return insertManagerPictureProcessingInfo(openId, filePathName, baseDir, templateFilePathName);
    }

    /**
     * 生成图片id
     *
     * @return 图片id
     */
    private String getPictureId() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String formatDate = format.format(new Date());
        Long increment = redisTemplate.opsForValue().increment(RedisKeyPrefix.PICTURE_ID.getKeyPrefix() + formatDate);
        redisTemplate.expire(RedisKeyPrefix.PICTURE_ID.getKeyPrefix() + formatDate, RedisKeyPrefix.PICTURE_ID.getExpire(), TimeUnit.SECONDS);
        return PICTURE_ID_PREFIX + formatDate + String.format("%0" + 9 + "d", increment);
    }

    @Override
    public ResultBean uploadFile(String base64File, String openId) {
        String baseDir = ProjectConfig.PROJECT_CONFIG.get(Constant.PHOTO_UPLOAD_BASE_DIR.getValue(String.class));
        if (StringUtils.isBlank(baseDir)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请设置文件上传基础目录");
        }
        String filePathName;
        try {
            filePathName = FileUtils.upload(baseDir, base64File, "jpg");
        } catch (IOException e) {
            log.error("uploadFile=>上传失败", e);
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
        if (StringUtils.isBlank(filePathName)) {
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
        return insertManagerPictureProcessingInfo(openId, filePathName, baseDir);
    }

    private ResultBean insertManagerPictureProcessingInfo(String openId, String filePathName, String baseDir) {
        ManagerPictureProcessingInfo info = new ManagerPictureProcessingInfo();
        info.setOpenId(openId);
        info.setPicturePath(filePathName);
        String id = getPictureId();
        info.setId(id);
        int i = managerPictureProcessingDao.insert(info);
        if (i > 0) {
            String baseUrl = ProjectConfig.PROJECT_CONFIG.get(Constant.IMAGE_BASE_URL.getValue(String.class));
            info.setPicturePath(baseUrl + info.getPicturePath());
            return new ResultBean(info);
        } else {
            FileUtils.delFile(baseDir, filePathName);
            return new ResultBean(ResultBean.FAIL_CODE, "上传失败");
        }
    }

    private ResultBean insertManagerPictureProcessingInfo(String openId, String filePathName, String baseDir, String templateFilePathName) {
        ManagerPictureProcessingInfo info = new ManagerPictureProcessingInfo();
        info.setOpenId(openId);
        info.setPicturePath(filePathName);
        info.setPictureTemplatePath(templateFilePathName);
        String id = getPictureId();
        info.setId(id);
        int i = managerPictureProcessingDao.insert(info);
        if (i > 0) {
            String baseUrl = ProjectConfig.PROJECT_CONFIG.get(Constant.IMAGE_BASE_URL.getValue(String.class));
            info.setPicturePath(baseUrl + filePathName);
            info.setPictureTemplatePath(baseUrl + templateFilePathName);
            return new ResultBean(info);
        } else {
            FileUtils.delFile(baseDir, filePathName);
            FileUtils.delFile(baseDir, templateFilePathName);
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
