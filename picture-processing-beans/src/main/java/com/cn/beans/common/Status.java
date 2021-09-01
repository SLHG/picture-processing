package com.cn.beans.common;

public enum Status {
    //可用
    IS_ENABLE(1),
    //不可用
    NOT_ENABLE(0);
    private final int status;

    Status(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
