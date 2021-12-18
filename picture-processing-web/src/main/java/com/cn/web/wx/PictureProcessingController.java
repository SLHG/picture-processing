package com.cn.web.wx;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.cn.beans.common.Constant;
import com.cn.beans.common.RedisKeyPrefix;
import com.cn.beans.common.ResultBean;
import com.cn.beans.photo.BodySegBean;
import com.cn.service.config.ProjectConfig;
import com.cn.service.wx.PictureProcessingService;
import com.cn.web.BDFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/wx/photo")
@Scope("prototype")
@Slf4j
public class PictureProcessingController {

    AipBodyAnalysis aipBodyAnalysis = BDFactory.getAipBodyAnalysis();

    final
    RedisTemplate<String, Object> redisTemplate;
    final
    PictureProcessingService pictureProcessingService;

    public PictureProcessingController(RedisTemplate<String, Object> redisTemplate, PictureProcessingService pictureProcessingService) {
        this.redisTemplate = redisTemplate;
        this.pictureProcessingService = pictureProcessingService;
    }

    /**
     * 获取前景图片
     *
     * @param file 图片文件
     * @return 去除前景base64图片
     */
    @PostMapping(value = "/getForeground")
    public ResultBean getForeground(@RequestParam(value = "file") MultipartFile file, String openId) {
        Object sessionKey = redisTemplate.opsForValue().get(RedisKeyPrefix.WX_SESSION_KEY.getKeyPrefix() + openId);
        if (StringUtils.isBlank((String) sessionKey)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请重新授权");
        }
        ResultBean result = checkFile(file);
        if (result != null) {
            return result;
        }
        HashMap<String, String> options = new HashMap<>();
        options.put("type", "foreground");
        try {
            JSONObject object = aipBodyAnalysis.bodySeg(file.getBytes(), options);
            BodySegBean bodySegBean = JSON.parseObject(object.toString(), BodySegBean.class);
            if (bodySegBean.getPerson_num() >= 1) {
                return pictureProcessingService.uploadFile(bodySegBean.getForeground(), openId);
            } else {
                return new ResultBean(ResultBean.FAIL_CODE, "未识别人脸");
            }
        } catch (Exception e) {
            log.info("背景图变化接口出错了", e);
            return new ResultBean(ResultBean.FAIL_CODE, "系统错误");
        }
    }

    private ResultBean checkFile(MultipartFile file) {
        if (file == null) {
            return new ResultBean(ResultBean.FAIL_CODE, "文件缺失");
        }
        String maxSize = ProjectConfig.PROJECT_CONFIG.get(Constant.UPLOAD_FILE_MAX_SIZE.getValue(String.class));
        long size;
        if (StringUtils.isBlank(maxSize)) {
            size = Constant.DEFAULT_MAX_SIZE.getValue(int.class);
        } else {
            size = Long.parseLong(maxSize);
        }
        if (file.getSize() > size) {
            return new ResultBean(ResultBean.FAIL_CODE, "图片超出最大可上传限制");
        }
        return null;
    }

    /**
     * 处理图片上传
     *
     * @param file 图片文件
     * @return 上传结果
     */
    @PostMapping(value = "/uploadFile")
    public ResultBean uploadFile(@RequestParam(value = "file") MultipartFile file, String openId) {
        Object sessionKey = redisTemplate.opsForValue().get(RedisKeyPrefix.WX_SESSION_KEY.getKeyPrefix() + openId);
        if (StringUtils.isBlank((String) sessionKey)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请重新授权");
        }
        ResultBean result = checkFile(file);
        if (result != null) {
            return result;
        }
        return pictureProcessingService.uploadFile(file, openId);
    }

    /**
     * 获取相框列表
     *
     * @param openId 用户微信id
     */
    @GetMapping(value = "/getFrameList")
    public ResultBean getFrameList(String openId) {
        Object sessionKey = redisTemplate.opsForValue().get(RedisKeyPrefix.WX_SESSION_KEY.getKeyPrefix() + openId);
        if (StringUtils.isBlank((String) sessionKey)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请重新授权");
        }
        return pictureProcessingService.getFrameList();
    }

    /**
     * 获取挂件列表
     *
     * @param openId 用户微信id
     */
    @GetMapping(value = "/getPendantList")
    public ResultBean getPendantList(String openId) {
        Object sessionKey = redisTemplate.opsForValue().get(RedisKeyPrefix.WX_SESSION_KEY.getKeyPrefix() + openId);
        if (StringUtils.isBlank((String) sessionKey)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请重新授权");
        }
        return pictureProcessingService.getPendantList();
    }

}
