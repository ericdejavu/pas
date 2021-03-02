package com.homework.pas.common.respnose;

public interface ResponseCode {
    /**
     * 返回错误码
     *
     * @return
     */
    int getCode();

    /**
     * 返回错误信息
     *
     * @return
     */
    String getMsg();
}
