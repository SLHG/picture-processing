package com.cn.web.manager;

import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerUserInfo;
import com.cn.service.manger.ManagerUserInfoService;
import lombok.extern.slf4j.Slf4j;
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
        return new ResultBean(managerUserInfoService.getList(start, page));
    }

    @DeleteMapping("/delete")
    public ResultBean delete(String userName) {
        int num = managerUserInfoService.delete(userName);
        if (num > 0) {
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }

    @PostMapping("/save")
    public ResultBean save(@RequestBody ManagerUserInfo managerUserInfo) {
        if (managerUserInfo.isBlack()) {
            return new ResultBean(ResultBean.FAIL_CODE, "参数错误");
        }
        int num = managerUserInfoService.save(managerUserInfo);
        if (num > 0) {
            return new ResultBean();
        }
        return new ResultBean(ResultBean.FAIL_CODE, ResultBean.FAIL_MSG);
    }
}
