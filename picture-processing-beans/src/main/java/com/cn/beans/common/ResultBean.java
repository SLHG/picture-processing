package com.cn.beans.common;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResultBean {
    public static final String SUCCESS_CODE = "0";
    public static final String FAIL_CODE = "-9999";
    public static final String SUCCESS_MSG = "成功";
    public static final String FAIL_MSG = "失败";
    private String rtnCode = SUCCESS_CODE;
    private String rtnMsg = SUCCESS_MSG;
    private Object result;
    private List<?> resultList;
    private PageInfo<?> pageInfo;

    public ResultBean(String rtnCode, String rtnMsg) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
    }

    public ResultBean(Object result) {
        this.result = result;
    }
}
