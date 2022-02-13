package com.cn.dao.manager;

import com.cn.beans.manager.ManagerPictureProcessingInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ManagerPictureProcessingDao {


    List<ManagerPictureProcessingInfo> getList(@Param("id") String id);

    int insert(ManagerPictureProcessingInfo info);

    List<ManagerPictureProcessingInfo> getListByIds(@Param("list") Set<String> idSet);

    List<ManagerPictureProcessingInfo> getListLikeId(@Param("id") String id);

    int delete(@Param("id") String id);

    ManagerPictureProcessingInfo selectById(@Param("id") String id);

    int updatePictureTemplatePath(@Param("id") String pictureId, @Param("templateFilePath") String templateFilePath);
}
