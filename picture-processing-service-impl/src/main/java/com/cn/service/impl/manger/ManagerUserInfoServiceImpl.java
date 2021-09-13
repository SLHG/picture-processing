package com.cn.service.impl.manger;

import com.cn.beans.manager.ManagerUserInfo;
import com.cn.dao.manager.ManagerUserInfoDao;
import com.cn.service.manger.ManagerUserInfoService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerUserInfoServiceImpl implements ManagerUserInfoService {

    final
    ManagerUserInfoDao managerUserInfoDao;
    final
    PasswordEncoder passwordEncoder;

    public ManagerUserInfoServiceImpl(ManagerUserInfoDao managerUserInfoDao, PasswordEncoder passwordEncoder) {
        this.managerUserInfoDao = managerUserInfoDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ManagerUserInfo> getList() {
        return managerUserInfoDao.getList();
    }

    @Override
    public int delete(String userName) {
        return managerUserInfoDao.delete(userName);
    }

    @Override
    public int save(ManagerUserInfo managerUserInfo) {
        //对用户密码加密存储
        managerUserInfo.setPassWord(passwordEncoder.encode(managerUserInfo.getPassWord()));
        ManagerUserInfo info = managerUserInfoDao.getUserInfoByUserName(managerUserInfo.getUserName());
        if (info == null) {
            return managerUserInfoDao.insert(managerUserInfo);
        }
        return managerUserInfoDao.update(managerUserInfo);
    }

    @Override
    public ManagerUserInfo getUserInfoByUserName(String userName) {
        return managerUserInfoDao.getUserInfoByUserName(userName);
    }
}
