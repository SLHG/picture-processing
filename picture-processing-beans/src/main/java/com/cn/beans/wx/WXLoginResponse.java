package com.cn.beans.wx;

import lombok.Data;

@Data
public class WXLoginResponse {

    //微信openId
    private String openId;
    //会话密钥
    private String session_key;
    //用户在开放平台唯一标识符
    private String unionid;
    //错误码
    private int errcode;
    //错误信息
    private String errmsg;


}
