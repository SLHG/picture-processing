package com.cn.service.wx;

import com.cn.beans.common.ResultBean;
import com.cn.beans.wx.WXUserInfo;

public interface WXUserInfoService {
    WXUserInfo getByOpenId(String openId);

    ResultBean login(String code);

    int save(WXUserInfo info);
}
