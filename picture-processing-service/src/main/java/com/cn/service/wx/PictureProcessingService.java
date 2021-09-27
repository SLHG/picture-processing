package com.cn.service.wx;

import com.cn.beans.common.ResultBean;
import org.springframework.web.multipart.MultipartFile;

public interface PictureProcessingService {

    ResultBean uploadFile(MultipartFile file, String openId);
}
