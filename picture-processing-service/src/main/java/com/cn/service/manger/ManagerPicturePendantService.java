package com.cn.service.manger;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPicturePendantInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface ManagerPicturePendantService {
    PageInfo<ManagerPicturePendantInfo> getList(int start, int page);

    ResultBean uploadFile(MultipartFile file, String pendantName);

    ResultBean delete(String id);
}
