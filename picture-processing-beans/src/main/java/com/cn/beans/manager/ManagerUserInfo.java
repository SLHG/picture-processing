package com.cn.beans.manager;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ManagerUserInfo {

    private String userName;
    private String passWord;
    private String authority;


    public boolean isBlack() {
        return StringUtils.isBlank(this.getUserName()) || StringUtils.isBlank(this.getPassWord());
    }
}
