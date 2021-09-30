package com.cn.dao.manager;

import com.cn.beans.manager.ManagerPicturePendantInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerPicturePendantDao {


    List<ManagerPicturePendantInfo> getList();

    int insert(ManagerPicturePendantInfo info);

    int delete(@Param("id") String id);

    ManagerPicturePendantInfo selectById(@Param("id") String id);
}
