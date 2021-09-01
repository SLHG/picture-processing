package com.cn.beans.photo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class BodySegBean {
    private int person_num;
    private String foreground;
    private long log_id;
    private List person_info;
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
