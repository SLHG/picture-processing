package com.cn.beans.manager;

import com.cn.utils.SecurityUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class ManagerUserInfo {

    private String userName;
    private String passWord;
    private String authority;
    private String authorityName;
    private String createTime;


    public boolean checkParams() {
        if (StringUtils.isBlank(this.getUserName()) || StringUtils.isBlank(this.getPassWord()) || StringUtils.isBlank(this.getAuthority())) {
            return true;
        }
        String authority = SecurityUtils.AUTHORITY_MAP.get(this.getAuthority().toUpperCase());
        if (StringUtils.isBlank(authority)) {
            return true;
        }
        this.setAuthority(authority);
        return false;
    }
}
