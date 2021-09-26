package com.cn.service.impl.manger;

import com.cn.beans.manager.ManagerPictureProcessingInfo;
import com.cn.dao.manager.ManagerPictureProcessingDao;
import com.cn.service.manger.ManagerPictureProcessingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

@Service
public class ManagerPictureProcessingServiceImpl implements ManagerPictureProcessingService {

    final
    ManagerPictureProcessingDao managerPictureProcessingDao;


    public ManagerPictureProcessingServiceImpl(ManagerPictureProcessingDao managerPictureProcessingDao) {
        this.managerPictureProcessingDao = managerPictureProcessingDao;
    }

    @Override
    public PageInfo<ManagerPictureProcessingInfo> getList(int start, int page, String id) {
        PageHelper.startPage(start, page);
        return new PageInfo<>(managerPictureProcessingDao.getList(id));
    }

}
