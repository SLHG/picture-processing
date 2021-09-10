package com.cn.dao.manager;

import com.cn.beans.manager.ManagerUserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerUserInfoDao {
    ManagerUserInfo getUserInfoByUserName(@Param("userName") String userName);

    List<ManagerUserInfo> getList();

    int delete(@Param("userName") String userName);

    int insert(ManagerUserInfo managerUserInfo);

    int update(ManagerUserInfo managerUserInfo);
}
