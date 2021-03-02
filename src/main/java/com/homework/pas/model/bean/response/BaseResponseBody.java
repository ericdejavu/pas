package com.homework.pas.model.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.homework.pas.common.respnose.ResponseCode;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BaseResponseBody {
    private int code;
    private String message;
    private Object data;

    public BaseResponseBody(ResponseCode code) {
        setCode(code);
    }

    public BaseResponseBody(ResponseCode code, Object data) {
        setCode(code);
        this.data = data;
    }

    public void setCode(ResponseCode code) {
        if (code == null) {
            this.code = 500;
            this.message = "unknown error from server";
        } else {
            this.code = code.getCode();
            this.message = code.getMsg();
        }
    }
}
