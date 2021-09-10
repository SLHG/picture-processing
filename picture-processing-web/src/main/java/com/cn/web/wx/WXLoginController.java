package com.cn.web.wx;

import com.cn.beans.common.RedisKeyPrefix;
import com.cn.beans.common.ResultBean;
import com.cn.beans.wx.WXUserInfo;
import com.cn.service.wx.WXUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/wx")
@Slf4j
public class WXLoginController {

    final
    WXUserInfoService wxUserInfoService;
    final
    RedisTemplate<String, Object> redisTemplate;

    public WXLoginController(WXUserInfoService wxUserInfoService, RedisTemplate<String, Object> redisTemplate) {
        this.wxUserInfoService = wxUserInfoService;
        this.redisTemplate = redisTemplate;
    }

    @GetMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public ResultBean login(String code) {
        log.info("login=>用户code" + code);
        if (StringUtils.isBlank(code)) {
            return new ResultBean(ResultBean.FAIL_CODE, "登录code为空");
        }
        return wxUserInfoService.login(code);
    }

    @PostMapping("/save")
    public ResultBean save(@RequestBody WXUserInfo info) {
        log.info("save=>保存/更新用户信息{}", info);
        if (StringUtils.isBlank(info.getOpenId())) {
            return new ResultBean(ResultBean.FAIL_CODE, "参数错误");
        }
        Object sessionKey = redisTemplate.opsForValue().get(RedisKeyPrefix.WX_SESSION_KEY.getKeyPrefix() + info.getOpenId());
        if (StringUtils.isBlank((String) sessionKey)) {
            return new ResultBean(ResultBean.FAIL_CODE, "请重新授权");
        }
        int num = wxUserInfoService.save(info);
        if (num > 0) {
            new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, "更新失败");
    }

}
