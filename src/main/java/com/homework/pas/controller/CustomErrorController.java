package com.homework.pas.controller;

import com.homework.pas.common.respnose.ServletRetCode;
import com.homework.pas.model.bean.response.BaseResponseBody;
import io.swagger.annotations.Api;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "服务器异常处理接口", description = "CustomErrorController")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CustomErrorController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    @ResponseBody
    public BaseResponseBody getError() {
        return new BaseResponseBody(ServletRetCode.SERVER_ROUTER_NOT_FOUND);
    }

}
