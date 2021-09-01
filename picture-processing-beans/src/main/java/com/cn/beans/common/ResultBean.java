package com.cn.beans.common;

import java.util.List;

public class ResultBean {
    public static final String SUCCESS_CODE = "0";
    public static final String FAIL_CODE = "-9999";
    public static final String SUCCESS_MSG = "成功";
    public static final String FAIL_MSG = "失败";
    private String rtnCode = SUCCESS_CODE;
    private String rtnMsg = SUCCESS_MSG;
    private Object result;
    private List<?> resultList;

    public ResultBean(String rtnCode, String rtnMsg) {
        this.rtnCode = rtnCode;
        this.rtnMsg = rtnMsg;
    }

    public ResultBean(Object result) {
        this.result = result;
    }

    public ResultBean() {
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public List<?> getResultList() {
        return resultList;
    }

    public void setResultList(List<?> resultList) {
        this.resultList = resultList;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "rtnCode='" + rtnCode + '\'' +
                ", rtnMsg='" + rtnMsg + '\'' +
                ", result=" + result +
                ", resultList=" + resultList +
                '}';
    }
}
