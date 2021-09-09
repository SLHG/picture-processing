package com.cn.web.wx;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cn.beans.common.ResultBean;
import com.cn.beans.wx.WXUserInfo;
import com.cn.config.ProjectConfig;
import com.cn.service.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/wx")
@Scope("prototype")
@Slf4j
public class WXLoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WXLoginController.class);

    private static final String APP_ID = "wxfd60f98a21eea9ba";
    private static final String SECRET = "9a23e312240bf17b629902875e6fb7ce";
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";


    @GetMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResultBean login(String code) {
        LOGGER.info("login=>用户code" + code);
        if (StringUtils.isBlank(code)) {
            return new ResultBean(ResultBean.FAIL_CODE, "登录code为空");
        }
        List<NameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("appid", ProjectConfig.PROJECT_CONFIG.get("wx_appid")));
        list.add(new BasicNameValuePair("secret", ProjectConfig.PROJECT_CONFIG.get("wx_secret")));
        list.add(new BasicNameValuePair("js_code", code));
        list.add(new BasicNameValuePair("grant_type", "authorization_code"));
        String response = HttpUtils.httpGet(ProjectConfig.PROJECT_CONFIG.get("wx_login_url"), list);
        LOGGER.info(response);
        JSONObject jsonObject = JSON.parseObject(response);
        return new ResultBean(jsonObject.getString("openid"));
    }

    @PostMapping("/save")
    public ResultBean save(@RequestBody WXUserInfo info) {
        LOGGER.info("save=>保存用户信息{}", info);
        return new ResultBean();
    }

}
