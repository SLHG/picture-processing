package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerUserInfo;
import com.cn.service.manger.ManagerUserInfoService;
import com.cn.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/manager/user")
@Slf4j
public class ManagerUserInfoController {

    final
    ManagerUserInfoService managerUserInfoService;

    public ManagerUserInfoController(ManagerUserInfoService managerUserInfoService) {
        this.managerUserInfoService = managerUserInfoService;
    }

    @GetMapping("/get")
    public ResultBean getList(@RequestParam(defaultValue = "1") int start, @RequestParam(defaultValue = "10") int page) {
        if (SecurityUtils.isAdmin(SecurityContextHolder.getContext())) {
            return new ResultBean(managerUserInfoService.getList(start, page));
        }
        String userName = SecurityUtils.getUserName(SecurityContextHolder.getContext());
        return new ResultBean(managerUserInfoService.getUserListByUserName(userName, start, page));
    }

    @DeleteMapping("/delete")
    public ResultBean delete(@RequestParam String userName) {
        String name = SecurityUtils.getUserName(SecurityContextHolder.getContext());
        if (userName.equals(name)) {
            return new ResultBean(ResultBean.FAIL_CODE, "无法删除当前登录用户");
        }
        int num = managerUserInfoService.delete(userName);
        if (num > 0) {
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }

    @PostMapping("/add")
    public ResultBean add(@RequestBody ManagerUserInfo managerUserInfo) {
        if (managerUserInfo.checkParams()) {
            return new ResultBean(ResultBean.FAIL_CODE, "参数错误");
        }
        return managerUserInfoService.add(managerUserInfo);
    }

    @PostMapping("/save")
    public ResultBean save(@RequestBody ManagerUserInfo managerUserInfo) {
        if (managerUserInfo.checkParams()) {
            return new ResultBean(ResultBean.FAIL_CODE, "参数错误");
        }
        if (SecurityUtils.isAdmin(SecurityContextHolder.getContext())) {
            return managerUserInfoService.save(managerUserInfo);
        }
        String userName = SecurityUtils.getUserName(SecurityContextHolder.getContext());
        if (!managerUserInfo.getUserName().equals(userName)) {
            return new ResultBean(ResultBean.FAIL_CODE, "无权修改");
        }
        String userAuthority = SecurityUtils.getUserAuthority(SecurityContextHolder.getContext());
        managerUserInfo.setAuthority(userAuthority);
        return managerUserInfoService.save(managerUserInfo);
    }
}
