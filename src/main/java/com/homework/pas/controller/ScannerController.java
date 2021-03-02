package com.homework.pas.controller;

import com.homework.pas.model.bean.response.BaseResponseBody;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ScannerController {


    @PostMapping("parseRecord")
    public BaseResponseBody parseRecord(@RequestParam String record) {
        return new BaseResponseBody();
    }

    @GetMapping("getGoodsReport")
    public BaseResponseBody getGoodsReport() {
        return new BaseResponseBody();
    }

    @GetMapping("getValidRecordLogs")
    public BaseResponseBody getValidRecordLogs() {
        return new BaseResponseBody();
    }

}
