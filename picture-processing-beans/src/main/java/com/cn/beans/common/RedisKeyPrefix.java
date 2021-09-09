package com.cn.beans.common;

public enum RedisKeyPrefix {
    //微信密钥
    WX_SESSION_KEY("wx:session:", "用户微信会话密钥", "key", "wx:session:123", 3600);

    //key前缀
    private final String keyPrefix;
    //描述说明
    private final String desc;
    //类型,枚举值 key,field
    private final String type;
    //举例
    private final String example;
    //失效时间
    private final Integer expire;

    RedisKeyPrefix(String keyPrefix, String desc, String type, String example, Integer expire) {
        this.keyPrefix = keyPrefix;
        this.desc = desc;
        this.type = type;
        this.example = example;
        this.expire = expire;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public String getDesc() {
        return desc;
    }

    public String getType() {
        return type;
    }

    public String getExample() {
        return example;
    }

    public Integer getExpire() {
        return expire;
    }
}
