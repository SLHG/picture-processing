package com.cn.beans.photo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class BodySegBean {
    //检测到人体框数目
    private int person_num;
    //分割后的人像前景抠图,透明背景
    private String foreground;
    //唯一的log id,用于问题定位
    private long log_id;
    //人体框信息
    private List<Object> person_info;

    @NoArgsConstructor
    @Data
    public static class PersonInfoBean {
        private double height;
        private double width;
        private double top;
        private double score;
        private double left;
    }
}
