package com.homework.pas.controller;

import com.homework.pas.common.respnose.ResponseCode;
import com.homework.pas.model.bean.response.BaseResponseBody;
import com.homework.pas.service.ScannerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ScannerController {

    @Resource
    private ScannerService scannerService;

    @PostMapping("parseRecord")
    public BaseResponseBody parseRecord(@RequestParam String record) {
        return new BaseResponseBody(scannerService.parseRecord(record));
    }

    @GetMapping("getGoodsReport")
    public BaseResponseBody getGoodsReport() {
        return scannerService.getGoodsReport();
    }

    @GetMapping("getValidRecordLogs")
    public BaseResponseBody getValidRecordLogs() {
        return scannerService.getValidRecordLogs();
    }

}
