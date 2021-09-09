package com.cn.dao.wx;

import com.cn.beans.wx.WXUserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WXUserInfoDao {

    WXUserInfo getByOpenId(@Param("openId") String openId);

    int insert(WXUserInfo info);

    int update(WXUserInfo info);
}
