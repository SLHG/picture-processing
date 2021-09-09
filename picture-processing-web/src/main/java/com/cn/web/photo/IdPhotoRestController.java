package com.cn.web.photo;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.cn.beans.common.ResultBean;
import com.cn.beans.photo.BodySegBean;
import com.cn.web.BDFactory;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/photo")
@Scope("prototype")
@Slf4j
public class IdPhotoRestController {

    AipBodyAnalysis aipBodyAnalysis = BDFactory.getAipBodyAnalysis();

    /**
     * 获取前景图片
     *
     * @param file 图片文件
     * @return 去除前景base64图片
     */
    @PostMapping(value = "/getForeground", produces = "application/json;charset=UTF-8")
    public ResultBean getForeground(@RequestParam(value = "file") MultipartFile file) {
        HashMap<String, String> options = new HashMap<>();
        options.put("type", "foreground");
        try {
            JSONObject object = aipBodyAnalysis.bodySeg(file.getBytes(), options);
            BodySegBean bodySegBean = JSON.parseObject(object.toString(), BodySegBean.class);
            if (bodySegBean.getPerson_num() >= 1) {
                return new ResultBean(bodySegBean.getForeground());
            } else {
                return new ResultBean(ResultBean.FAIL_CODE, "未识别人脸");
            }
        } catch (Exception e) {
            log.info("背景图变化接口出错了", e);
            return new ResultBean(ResultBean.FAIL_CODE, "系统错误");
        }
    }

    private Color getColor(String colorStr) {
        Color backgroudColor = null;
        if (colorStr.equals("red")) {
            backgroudColor = Color.RED;
        }
        if (colorStr.equals("blue")) {
            backgroudColor = Color.BLUE;
        }
        if (colorStr.equals("black")) {
            backgroudColor = Color.BLACK;
        }
        if (colorStr.equals("white")) {
            backgroudColor = Color.WHITE;
        }
        return backgroudColor;
    }

}
