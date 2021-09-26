package com.cn.service.manger;

import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.cn.beans.manager.ManagerUserInfo;
import com.github.pagehelper.PageInfo;

public interface ManagerPictureProcessingService {
    PageInfo<ManagerPictureProcessingInfo> getList(int start, int page, String id);
}
