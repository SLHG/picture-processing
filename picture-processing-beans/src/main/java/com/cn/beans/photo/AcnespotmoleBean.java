package com.cn.beans.photo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AcnespotmoleBean extends BaseBean {
    //具体返回的内容
    private Data data;

    @lombok.Data
    public static class Data {
        private String image_base64;
        private Integer acne_count = 0;
        private Integer speckle_count = 0;
        private Integer mole_count = 0;
    }

    public AcnespotmoleBean success(String msg, String msg_zh, Data data) {
        this.msg = msg;
        this.msg_zh = msg_zh;
        this.code = 200;
        this.data = data;
        return this;
    }

    public AcnespotmoleBean fail(String msg, String msg_zh, Integer code) {
        this.msg = msg;
        this.msg_zh = msg_zh;
        this.code = code;
        return this;
    }

    public AcnespotmoleBean error(String msg, String msg_zh) {
        this.msg = msg;
        this.msg_zh = msg_zh;
        this.code = 500;
        return this;
    }
}
