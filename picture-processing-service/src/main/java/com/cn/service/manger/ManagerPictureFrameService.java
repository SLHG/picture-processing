package com.cn.service.manger;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerPictureFrameInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface ManagerPictureFrameService {
    PageInfo<ManagerPictureFrameInfo> getList(int start, int page);

    ResultBean uploadFile(MultipartFile file, String frameName);

    ResultBean delete(String id);
}
