package com.homework.pas.exception;


import com.homework.pas.common.respnose.ServletRetCode;
import com.homework.pas.common.util.TraceMessageUtils;
import com.homework.pas.model.bean.response.BaseResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseResponseBody exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("{}", TraceMessageUtils.getMessage(e));
        return new BaseResponseBody(ServletRetCode.SERVER_ERROR_BUSY);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public BaseResponseBody methodExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("{}", TraceMessageUtils.getMessage(e));
        return new BaseResponseBody(ServletRetCode.SERVER_ERROR_METHOD);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    @ResponseBody
    public BaseResponseBody paramExceptionHandler(HttpServletRequest request, Exception e) {
        log.error("{}", TraceMessageUtils.getMessage(e));
        return new BaseResponseBody(ServletRetCode.SERVER_ERROR_ROUTER_PARAM);
    }
}
