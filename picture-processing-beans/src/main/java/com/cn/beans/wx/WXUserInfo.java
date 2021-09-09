package com.cn.beans.wx;

import lombok.Data;

@Data
public class WXUserInfo {

    //微信openId
    private String openId;
    //用户手机号
    private String mobile;
    //用户昵称
    private String nickName;
    //用户性别,0-未知,1-男,2-女
    private int gender;
    //用户所在国家
    private String country;
    //用户所在省份
    private String province;
    //用户所在城市
    private String city;
    //创建时间
    private String createTime;
    //是否包含用户信息
    private boolean hasUserInfo;

}
