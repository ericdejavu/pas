package com.homework.pas.service;

import com.homework.pas.common.respnose.ResponseCode;
import com.homework.pas.common.util.DiaryRecordParser;
import com.homework.pas.model.bean.response.BaseResponseBody;
import com.homework.pas.model.entity.ScannerRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ScannerService {

    @Resource
    private DiaryRecordParser diaryRecordParser;

    public ResponseCode parseRecord(String record) {
        ScannerRecord scannerRecord = new ScannerRecord();
        ResponseCode responseCode = diaryRecordParser.parseToScannerRecord(record, scannerRecord);
        return responseCode;
    }


    public BaseResponseBody getGoodsReport() {
        // data : List<GoodsReport>
        return null;
    }

    public BaseResponseBody getValidRecordLogs() {
        // data : List<ScannerRecord>
        return null;
    }
}
