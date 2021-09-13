package com.cn.config;

import com.alibaba.fastjson.JSON;
import com.cn.beans.common.ResultBean;
import com.cn.beans.manager.ManagerUserInfo;
import com.cn.service.manger.ManagerUserInfoService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    final
    ManagerUserInfoService managerUserInfoService;

    public LoginSuccessHandler(ManagerUserInfoService managerUserInfoService) {
        this.managerUserInfoService = managerUserInfoService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response, Authentication authentication) throws IOException {
        String name = authentication.getName();
        ManagerUserInfo info = managerUserInfoService.getUserInfoByUserName(name);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if (info == null) {
            writer.write(JSON.toJSONString(new ResultBean(ResultBean.FAIL_CODE, "用户不存在")));
            writer.flush();
            writer.close();
            return;
        }
        info.setPassWord(null);
        writer.write(JSON.toJSONString(new ResultBean(info)));
        writer.flush();
        writer.close();
    }
}
