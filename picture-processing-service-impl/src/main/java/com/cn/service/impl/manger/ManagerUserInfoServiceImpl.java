package com.cn.service.impl.manger;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerUserInfo;
import com.cn.dao.manager.ManagerUserInfoDao;
import com.cn.service.manger.ManagerUserInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public PageInfo<ManagerUserInfo> getList(int start, int page) {
        PageHelper.startPage(start, page);
        return new PageInfo<>(managerUserInfoDao.getList());
    }

    @Override
    public int delete(String userName) {
        return managerUserInfoDao.delete(userName);
    }

    @Override
    public ResultBean save(ManagerUserInfo managerUserInfo) {
        ManagerUserInfo info = managerUserInfoDao.getUserInfoByUserName(managerUserInfo.getUserName());
        if (info == null) {
            return new ResultBean(ResultBean.FAIL_CODE, "用户不存在");
        }
        //对用户密码加密存储
        managerUserInfo.setPassWord(passwordEncoder.encode(managerUserInfo.getPassWord()));
        int update = managerUserInfoDao.update(managerUserInfo);
        if (update > 0) {
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }

    @Override
    public ManagerUserInfo getUserInfoByUserName(String userName) {
        return managerUserInfoDao.getUserInfoByUserName(userName);
    }

    @Override
    public ResultBean add(ManagerUserInfo managerUserInfo) {
        //对用户密码加密存储
        managerUserInfo.setPassWord(passwordEncoder.encode(managerUserInfo.getPassWord()));
        ManagerUserInfo info = managerUserInfoDao.getUserInfoByUserName(managerUserInfo.getUserName());
        if (info != null) {
            return new ResultBean(ResultBean.FAIL_CODE, "用户名已存在");
        }
        int insert = managerUserInfoDao.insert(managerUserInfo);
        if (insert > 0) {
            return new ResultBean();
        } else {
            return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
        }
    }

    @Override
    public PageInfo<ManagerUserInfo> getUserListByUserName(String userName, int start, int page) {
        PageHelper.startPage(start, page);
        return new PageInfo<>(managerUserInfoDao.getUserListByUserName(userName));
    }

}
