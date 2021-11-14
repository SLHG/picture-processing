package com.cn.beans.common;


import com.alibaba.fastjson.util.TypeUtils;

public enum Constant {
    PHOTO_UPLOAD_BASE_DIR("photo_upload_base_dir", "图片上传保存基路径"),
    FRAME_UPLOAD_BASE_DIR("frame_upload_base_dir", "相框上传保存基路径"),
    PENDANT_UPLOAD_BASE_DIR("pendant_upload_base_dir", "挂件上传保存基础路径"),
    DATE_FORMAT1("yyyy-MM-dd", "时间格式化1"),
    DATE_FORMAT2("yyyy-MM-dd HH:mm:ss", "时间格式化2"),
    UNDER_LINE("_", "下划线"),
    IFRAME_BASE_URL("iframe_base_url", "相框展示基础路径"),
    IMAGE_BASE_URL("image_base_url", "用户图片展示基础路径"),
    PENDANT_BASE_URL("pendant_base_url", "挂件图片展示基础路径"),
    DEFAULT_MAX_SIZE(10 * 1024 * 1024, "图片上传默认大小 10M"),
    UPLOAD_FILE_MAX_SIZE("upload_file_max_size", "上传图片配置大小参数key"),
    WX_APPID("wx_appid", "小程序id"),
    WX_SECRET("wx_secret", "小程序密钥"),
    WX_LOGIN_URL("wx_login_url", "小程序登录地址"),
    BAIDU_APPID("baidu_appid", "百度api对应id"),
    BAIDU_API_KEY("baidu_api_key", "百度api对应key"),
    BAIDU_SECRET_KEY("baidu_secret_key", "百度api对应密钥");

    private final Object value;
    private final String desc;

    Constant(Object value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public <E> E getValue(Class<E> clazz) {
        return TypeUtils.cast(value, clazz, null);
    }

    public String getDesc() {
        return desc;
    }

}
