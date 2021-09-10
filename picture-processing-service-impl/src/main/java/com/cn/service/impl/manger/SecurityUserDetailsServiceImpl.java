package com.cn.service.impl.manger;

import com.cn.beans.manager.ManagerUserInfo;
import com.cn.dao.manager.ManagerUserInfoDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailsServiceImpl implements UserDetailsService {

    final
    ManagerUserInfoDao managerUserInfoDao;

    public SecurityUserDetailsServiceImpl(ManagerUserInfoDao managerUserInfoDao) {
        this.managerUserInfoDao = managerUserInfoDao;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        if (StringUtils.isBlank(userName)) {
            throw new UsernameNotFoundException("user name is blank");
        }
        ManagerUserInfo info = managerUserInfoDao.getUserInfoByUserName(userName);
        if (info == null) {
            throw new UsernameNotFoundException("user not found");
        }
        List<GrantedAuthority> list = new ArrayList<>();
        String authority = info.getAuthority();
        list.add(new SimpleGrantedAuthority("ROLE_" + authority));
        return new User(info.getUserName(), info.getPassWord(), list);
    }
}
