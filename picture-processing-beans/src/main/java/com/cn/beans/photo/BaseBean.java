package com.cn.beans.photo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseBean {
    public Integer code;
    public String msg;
    public String msg_zh;
    public String author;
}
