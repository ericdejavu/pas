package com.homework.pas.controller;

import com.homework.pas.common.respnose.ParserRetCode;
import com.homework.pas.common.respnose.ResponseCode;
import com.homework.pas.model.bean.dto.BulkRecordDTO;
import com.homework.pas.model.bean.dto.RecordDTO;
import com.homework.pas.model.bean.response.BaseResponseBody;
import com.homework.pas.service.ScannerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class ScannerController {

    @Resource
    private ScannerService scannerService;


    @PostMapping("bulkParseRecord")
    public BaseResponseBody bulkParseRecord(@RequestBody BulkRecordDTO bulkRecordDTO) {
        if (bulkRecordDTO.getRecords() == null) {
            return new BaseResponseBody(ParserRetCode.PARSER_ERROR_PARAMS);
        }
        List<BaseResponseBody> responseBodies = new ArrayList<>();
        for (String record : bulkRecordDTO.getRecords()) {
            responseBodies.add(scannerService.parseRecord(record));
        }
        return new BaseResponseBody(ParserRetCode.PARSER_OPT_OK, responseBodies);
    }

    @PostMapping("parseRecord")
    public BaseResponseBody parseRecord(@RequestBody RecordDTO recordDTO) {
        return scannerService.parseRecord(recordDTO.getRecord());
    }

    @GetMapping("getGoodsReport")
    public BaseResponseBody getGoodsReport() {
        return scannerService.getGoodsReport();
    }

//    @GetMapping("getValidRecordLogs")
//    public BaseResponseBody getValidRecordLogs() {
//        return scannerService.getValidRecordLogs();
//    }

}
