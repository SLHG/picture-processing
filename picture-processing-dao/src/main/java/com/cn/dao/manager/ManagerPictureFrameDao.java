package com.cn.dao.manager;

import com.cn.beans.manager.ManagerPictureFrameInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerPictureFrameDao {


    List<ManagerPictureFrameInfo> getList();

    int insert(ManagerPictureFrameInfo info);

    int delete(@Param("id") String id);
}
