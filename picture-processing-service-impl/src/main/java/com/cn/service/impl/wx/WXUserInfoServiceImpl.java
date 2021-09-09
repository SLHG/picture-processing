package com.cn.service.impl.wx;

import com.alibaba.fastjson.JSON;
import com.cn.beans.common.RedisKeyPrefix;
import com.cn.beans.common.ResultBean;
import com.cn.beans.wx.WXLoginResponse;
import com.cn.beans.wx.WXUserInfo;
import com.cn.dao.wx.WXUserInfoDao;
import com.cn.service.config.ProjectConfig;
import com.cn.service.utils.HttpUtils;
import com.cn.service.wx.WXUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class WXUserInfoServiceImpl implements WXUserInfoService {
    final
    WXUserInfoDao wxUserInfoDao;
    final
    RedisTemplate<String, Object> redisTemplate;

    private static final int WX_RESPONSE_SUCCESS = 0;

    public WXUserInfoServiceImpl(WXUserInfoDao wxUserInfoDao, RedisTemplate<String, Object> redisTemplate) {
        this.wxUserInfoDao = wxUserInfoDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public WXUserInfo getByOpenId(String openId) {
        return wxUserInfoDao.getByOpenId(openId);
    }

    @Override
    public ResultBean login(String code) {
        List<NameValuePair> list = new ArrayList<>();
        //拼装请求微信获取openid接口的参数
        list.add(new BasicNameValuePair("appid", ProjectConfig.PROJECT_CONFIG.get("wx_appid")));
        list.add(new BasicNameValuePair("secret", ProjectConfig.PROJECT_CONFIG.get("wx_secret")));
        list.add(new BasicNameValuePair("js_code", code));
        list.add(new BasicNameValuePair("grant_type", "authorization_code"));
        //调用接口获取openid
        String response = HttpUtils.httpGet(ProjectConfig.PROJECT_CONFIG.get("wx_login_url"), list);
        WXLoginResponse wxLoginResponse = JSON.parseObject(response, WXLoginResponse.class);
        log.info("login=>登录返回结果:{}", wxLoginResponse);
        if (WX_RESPONSE_SUCCESS != wxLoginResponse.getErrcode()) {
            return new ResultBean(ResultBean.FAIL_CODE, "授权失败");
        }
        String openId = wxLoginResponse.getOpenId();
        //设置用户授权密钥到redis
        redisTemplate.opsForValue().set(RedisKeyPrefix.WX_SESSION_KEY.getKeyPrefix() + openId, wxLoginResponse.getSession_key(), RedisKeyPrefix.WX_SESSION_KEY.getExpire(), TimeUnit.SECONDS);
        //根据openid查询用户数据, 判断用户是否已注册
        WXUserInfo userInfo = wxUserInfoDao.getByOpenId(openId);
        if (userInfo == null) {
            WXUserInfo info = new WXUserInfo();
            info.setHasUserInfo(false);
            info.setOpenId(openId);
            ResultBean resultBean = new ResultBean(ResultBean.SUCCESS_CODE, "请登录");
            resultBean.setResult(info);
            return resultBean;
        }
        userInfo.setHasUserInfo(true);
        return new ResultBean(userInfo);
    }

    @Override
    public int save(WXUserInfo info) {
        WXUserInfo wxUserInfo = wxUserInfoDao.getByOpenId(info.getOpenId());
        if (wxUserInfo == null) {
            return wxUserInfoDao.insert(info);
        }
        return wxUserInfoDao.update(info);
    }
}
