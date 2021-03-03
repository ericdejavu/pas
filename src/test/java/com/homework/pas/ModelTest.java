package com.homework.pas;

import com.homework.pas.mapper.GoodsMapper;
import com.homework.pas.mapper.ScannerRecordMapper;
import com.homework.pas.mapper.StashMapper;
import com.homework.pas.model.entity.Goods;
import com.homework.pas.model.entity.ScannerRecord;
import com.homework.pas.model.entity.Stash;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@SpringBootTest
class ModelTest {

    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private ScannerRecordMapper scannerRecordMapper;

    @Resource
    private StashMapper stashMapper;

    private void goodsTest() {
        Goods g = goodsMapper.getIfExist("110");
        if (g != null) {
            log.info("110 already exist");
            return;
        }
        Goods goods = new Goods();
        goods.setName("balabala");
        goods.setPrice(new BigDecimal(100.00));
        goods.setQrcode("110");
        goodsMapper.save(goods);
    }

    private void scannerRecordTest() {
        // 2019-01-03 SALES 001 22
        // 2019-01-01 PURCHASE 001 50 3.00
        ScannerRecord scannerRecord1 = new ScannerRecord();
        scannerRecord1.setOptDate("2019-01-03");
        scannerRecord1.setOptCmd("SALES");
        scannerRecord1.setQrcode("001");
        scannerRecord1.setParam1("22");
        scannerRecordMapper.insert(scannerRecord1);

        ScannerRecord scannerRecord2 = new ScannerRecord();
        scannerRecord2.setOptDate("2019-01-01");
        scannerRecord2.setOptCmd("PURCHASE");
        scannerRecord2.setQrcode("001");
        scannerRecord2.setParam1("50");
        scannerRecord2.setParam2("3.00");
        scannerRecordMapper.insert(scannerRecord2);

        List<ScannerRecord> scannerRecords = scannerRecordMapper.selectAll();
        log.info("scannerRecords: {}", scannerRecords);
    }

    private void stashTest() {
        Stash stash = new Stash();
        stash.setQrcode("001");
        stash.setAmount(10);
        stash.setIsSoldOut(false);
        stash.setOptDate("2019-01-03");
        stash.setPay(new BigDecimal(50.50));
        stashMapper.insert(stash);

        List<Stash> stashes = stashMapper.selectByQrcodeAndOrderByOptDate("001", false);
        log.info("stashes: {}", stashes);
        stashes.clear();
        stashes = stashMapper.selectByQrcodeAndOrderByOptDate("001", true);
        log.info("stashes: {}", stashes);
    }

    @Test
    void contextLoads() {
        goodsTest();
        scannerRecordTest();
        stashTest();
    }

}
