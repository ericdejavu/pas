package com.homework.pas.common.util;

import com.homework.pas.common.respnose.ParserRetCode;
import com.homework.pas.common.respnose.ResponseCode;
import com.homework.pas.model.entity.ScannerRecord;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class DiaryRecordParser {

    @interface In {}
    @interface Out {}

    private static final List validCommands = new ArrayList() {
        {add("NEW"); add("PURCHASE"); add("SALES");}
    };
    private static final Integer MIN_COLS_SIZE = 4;
    private static final Integer MAX_PRICE_SIZE = 32;
    private static final Integer MAX_NAME_SIZE = 128;

    private boolean dateFormatCheck(String date) {
        return true;
    }

    private boolean commandCheck(String cmd) {
        return validCommands.contains(cmd);
    }

    public ResponseCode parseToScannerRecord(@In String record, @Out ScannerRecord scannerRecord) {
        if (record == null || record.isEmpty()) { return ParserRetCode.PARSER_ERROR_RECORD_CANT_BE_EMPTY; }
        record = record.trim();
        String [] cols = record.split(" ");
        if (cols.length < MIN_COLS_SIZE) { return ParserRetCode.PARSER_ERROR_RECORD_CANT_BE_EMPTY; }

        // check date
        if (!dateFormatCheck(cols[0])) {
            return ParserRetCode.PARSER_ERROR_RECORD_CANT_BE_EMPTY;
        }

        if (!commandCheck(cols[1])) {
            return ParserRetCode.PARSER_ERROR_RECORD_CANT_BE_EMPTY;
        }

        scannerRecord.setOptDate(cols[0]);
        scannerRecord.setOptCmd(cols[1]);
        scannerRecord.setQrcode(cols[2]);
        scannerRecord.setParam1(cols[3]);
        if (cols.length == MIN_COLS_SIZE + 1) {
            scannerRecord.setParam2(cols[4]);
        }

        return ParserRetCode.PARSER_OPT_OK;
    }

    public boolean checkName(String name) {
        return name == null || name.length() > MAX_NAME_SIZE;
    }

    public boolean checkPriceFormat(String price) {
        if (price == null || price.length() > MAX_PRICE_SIZE) { return false; }
        try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
