package com.cn.config;

import com.cn.beans.common.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class MyWebHandlerError implements HandlerExceptionResolver {

    @Override
    @NonNull
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, @NonNull HttpServletResponse httpServletResponse, Object o, @NonNull Exception e) {
        log.error(httpServletRequest.getRequestURI() + "=>系统报错:", e);
        ModelAndView error = new ModelAndView(new MappingJackson2JsonView());
        error.addObject("rtnCode", ResultBean.FAIL_CODE);
        error.addObject("rtnMsg", ResultBean.FAIL_MSG);
        return error;
    }
}
