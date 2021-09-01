package com.cn.web;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import com.baidu.aip.bodyanalysis.AipBodyAnalysis;
import com.cn.beans.photo.AcnespotmoleBean;
import com.cn.beans.photo.BodySegBean;
import com.cn.utils.PngColoringUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.HashMap;

@Controller
@RequestMapping(value = "/idphoto")
@Scope("prototype")
@Slf4j
public class IdPhotoRestController {

    AipBodyAnalysis aipBodyAnalysis = BDFactory.getAipBodyAnalysis();

    /**
     * 照片低色替换
     *
     * @param file    图片文件
     * @param request 请求
     * @return 替换结果
     */
    @GetMapping(value = "/replace", produces = "application/json;charset=UTF-8")
    public ResponseEntity idphotoReplace(@RequestParam(value = "file") MultipartFile file,
                                         HttpServletRequest request) {
        log.info("方法路径{}", request.getRequestURI());
        AcnespotmoleBean bean = new AcnespotmoleBean();
        //颜色
        String colorStr = ServletRequestUtils.getStringParameter(request, "color", "red");
        Color backgroudColor = getColor(colorStr);
        HashMap<String, String> options = new HashMap<>();
        try {
            options.put("type", "foreground");
            JSONObject object = aipBodyAnalysis.bodySeg(file.getBytes(), options);
            BodySegBean bodySegBean = JSON.parseObject(object.toString(), BodySegBean.class);
            if (bodySegBean.getPerson_num() >= 1) {
                //返回处理后的图片
                String imagebase64 = PngColoringUtil.changePNGBackgroudColorByBase64(Base64.decode(bodySegBean.getForeground()), backgroudColor);
                AcnespotmoleBean.Data data = new AcnespotmoleBean.Data();
                data.setImage_base64(imagebase64);
                bean.success("success", "成功", data);
            } else {
                bean.fail("fail", "处理失败 未检测到人脸", 20200522);
            }
        } catch (Exception e) {
            log.info("背景图变化接口出错了", e);
            bean.error("system error", "系统错误");
        }
        //响应的内容
        return new ResponseEntity(JSON.toJSONString(bean), HttpStatus.OK);
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
