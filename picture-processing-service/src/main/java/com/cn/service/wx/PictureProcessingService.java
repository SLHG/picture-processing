package com.cn.service.wx;

import com.cn.beans.common.ResultBean;
import org.springframework.web.multipart.MultipartFile;

public interface PictureProcessingService {

    ResultBean uploadFile(MultipartFile file, String openId, String photoType);

    ResultBean uploadFile(String base64File, String openId);

    ResultBean getFrameList(String frameType);

    ResultBean getPendantList();

    ResultBean uploadTemplateFile(MultipartFile templateFile, String pictureId);

}
