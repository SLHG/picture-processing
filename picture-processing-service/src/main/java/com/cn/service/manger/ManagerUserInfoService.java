package com.cn.service.manger;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerUserInfo;
import com.github.pagehelper.PageInfo;

public interface ManagerUserInfoService {
    PageInfo<ManagerUserInfo> getList(int start, int page);

    int delete(String userName);

    ResultBean save(ManagerUserInfo managerUserInfo);

    ManagerUserInfo getUserInfoByUserName(String userName);

    ResultBean add(ManagerUserInfo managerUserInfo);
}
