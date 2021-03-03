package com.homework.pas;

import com.homework.pas.common.util.DiaryRecordParser;
import com.homework.pas.model.entity.ScannerRecord;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class ParserTest {

    @Resource
    private DiaryRecordParser parser;

    String[] strs = new String[] {
            "2019-01-01 NEW 001 CocaCola 4.00",
            "2019-01-01 NEW 002 Orange 3.00",
            "2019-01-01 PURCHASE 001 50 3.00",
            "2019-01-01 PURCHASE 002 20 1.00",
            "2019-01-01 SALES 001 22",
            "2019-01-01 SALES 002 3",
            "2019-01-02 SALES 001 25",
            "2019-01-02 SALES 002 6",
            "2019-01-03 PURCHASE 001 20 3.50",
            "2019-01-03 SALES 001 22"
    };

    @Test
    public void testParser() {
        for (String s: strs) {
            ScannerRecord record = new ScannerRecord();
            parser.parseToScannerRecord(s, record);
            log.info("{}", record);
        }
    }


    String[] errorStrs = new String[] {
            "2019-01-01 NEW ",
            "2019-01-01 NEW 002 Orange",
            "2019-01-01 AAB 001 50 3.00",
            "2019-01-01 PURCHASE 002 20",
            "2019-01 SALES 001 22",
            "2019--01 SALES 002 3",
            "-01-02 SALES 001 25",
            "2019-01-02 SALES 002",
            "2019-01-03 001 20 3.50",
            "2019-01-03 SALES 22"
    };

    @Test
    public void testErrorParser() {
        for (String s: errorStrs) {
            ScannerRecord record = new ScannerRecord();
            parser.parseToScannerRecord(s, record);
            log.info("{}", record);
        }
    }

}
