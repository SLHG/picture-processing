package com.cn.service.manger;

import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ManagerPictureProcessingService {
    PageInfo<ManagerPictureProcessingInfo> getList(int start, int page, String id);

    void download(MultipartFile file, HttpServletResponse response) throws IOException;
}
