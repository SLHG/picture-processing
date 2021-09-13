package com.cn.service.manger;

import com.cn.beans.manager.ManagerUserInfo;

import java.util.List;

public interface ManagerUserInfoService {
    List<ManagerUserInfo> getList();

    int delete(String userName);

    int save(ManagerUserInfo managerUserInfo);

    ManagerUserInfo getUserInfoByUserName(String userName);
}
