package com.homework.pas.common.respnose;


public enum  ServletRetCode implements ResponseCode {
    /**
     *
     */
    SERVER_OPT_OK(0, "operation success"),
    SERVER_ERROR_BUSY(-7, "server busy"),
    SERVER_ERROR_METHOD(-8, "request method incorrect"),
    SERVER_ROUTER_NOT_FOUND(-9, "invalid request path"),
    SERVER_ERROR_ROUTER_PARAM(-10, "invalid request param");

    private int code;
    private String msg;
    ServletRetCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        RetCodeDispatcher.servletRetCodeMap.put(code, this);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
