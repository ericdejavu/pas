package com.homework.pas.service;

import com.homework.pas.common.respnose.ParserRetCode;
import com.homework.pas.common.respnose.ResponseCode;
import com.homework.pas.common.util.DiaryRecordParser;
import com.homework.pas.mapper.GoodsMapper;
import com.homework.pas.mapper.ScannerRecordMapper;
import com.homework.pas.mapper.StashMapper;
import com.homework.pas.model.bean.response.BaseResponseBody;
import com.homework.pas.model.entity.Goods;
import com.homework.pas.model.entity.ScannerRecord;
import com.homework.pas.model.entity.Stash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ScannerService {
    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private ScannerRecordMapper scannerRecordMapper;

    @Resource
    private StashMapper stashMapper;

    @Resource
    private DiaryRecordParser diaryRecordParser;


    private ResponseCode declareNewGoods(ScannerRecord scannerRecord) {
        if (scannerRecord == null) {
            return ParserRetCode.PARSER_ERROR_PARAMS;
        }
        Goods goods = goodsMapper.getIfExist(scannerRecord.getQrcode());
        if (goods != null) {
            return ParserRetCode.PARSER_ERROR_DUPLICATE_QRCODE;
        }

        if (!diaryRecordParser.checkPriceFormat(scannerRecord.getParam2())) {
            return ParserRetCode.PARSER_ERROR_PRICE_FORMAT_INCORRECT;
        }

        if (!diaryRecordParser.checkName(scannerRecord.getParam2())) {
            return ParserRetCode.PARSER_ERROR_NAME_FORMAT_INCORRECT;
        }

        goods = new Goods();
        goods.setName(scannerRecord.getParam1());
        goods.setQrcode(scannerRecord.getQrcode());
        goods.setPrice(new BigDecimal(scannerRecord.getParam2()));
        goodsMapper.save(goods);
        return ParserRetCode.PARSER_OPT_OK;
    }


    private ResponseCode purchaseGoods(ScannerRecord scannerRecord) {
        if (scannerRecord == null) {
            return ParserRetCode.PARSER_ERROR_PARAMS;
        }
        Goods goods = goodsMapper.getIfExist(scannerRecord.getQrcode());
        if (goods == null) {
            return ParserRetCode.PARSER_ERROR_NO_QRCODE_FOUND;
        }

        if (!diaryRecordParser.checkPriceFormat(scannerRecord.getParam2())) {
            return ParserRetCode.PARSER_ERROR_PRICE_FORMAT_INCORRECT;
        }

        if (!diaryRecordParser.checkDateFormat(scannerRecord.getOptDate())) {
            return ParserRetCode.PARSER_ERROR_DATE_FORMAT_INCORRECT;
        }

        if (!diaryRecordParser.checkQrcode(scannerRecord.getQrcode())) {
            return ParserRetCode.PARSER_ERROR_QRCODE_EMPTY;
        }

        if (!diaryRecordParser.checkAmount(scannerRecord.getParam1())) {
            return ParserRetCode.PARSER_ERROR_AMOUNT_FORMAT_INCORRECT;
        }

        Stash stash = new Stash();
        stash.setQrcode(scannerRecord.getQrcode());
        stash.setAmount(Integer.valueOf(scannerRecord.getParam1()));
        stash.setOptDate(scannerRecord.getOptDate());
        stash.setIsSoldOut(false);
        stash.setPay(new BigDecimal(scannerRecord.getParam2()));
        stashMapper.save(stash);
        return ParserRetCode.PARSER_OPT_OK;
    }

    private ResponseCode sale(ScannerRecord scannerRecord) {
        if (scannerRecord == null) {
            return ParserRetCode.PARSER_ERROR_PARAMS;
        }

        List<Stash> stashes = stashMapper.selectByQrcodeAndOrderByOptDate(scannerRecord.getQrcode());
        if (stashes.isEmpty()) {
            return ParserRetCode.PARSER_ERROR_NO_QRCODE_FOUND;
        }

        if (!diaryRecordParser.checkAmount(scannerRecord.getParam1())) {
            return ParserRetCode.PARSER_ERROR_AMOUNT_FORMAT_INCORRECT;
        }

        Integer soldNumber = Integer.valueOf(scannerRecord.getParam1());
        for (Stash stash : stashes) {
            soldNumber = soldNumber - stash.getAmount() - stash.getConsume();
            if (soldNumber >= 0) {
                stash.setConsume(stash.getAmount());
                stash.setIsSoldOut(true);
            } else {
                stash.setConsume(soldNumber);
            }
            stashMapper.save(stash);
        }

        return ParserRetCode.PARSER_OPT_OK;

    }

    @Transactional
    public synchronized ResponseCode parseRecord(String record) {
        ScannerRecord scannerRecord = new ScannerRecord();
        ResponseCode responseCode = diaryRecordParser.parseToScannerRecord(record, scannerRecord);
        if (responseCode.equals(ParserRetCode.PARSER_OPT_OK)) {
            switch (scannerRecord.getOptCmd()) {
                case "NEW":
                    responseCode = declareNewGoods(scannerRecord);
                    break;
                case "PURCHASE":
                    responseCode = purchaseGoods(scannerRecord);
                    break;
                case "SALES":
                    responseCode = sale(scannerRecord);
                    break;
                default:
                    break;
            }
        }
        if (responseCode.equals(ParserRetCode.PARSER_OPT_OK)) {
            scannerRecordMapper.insert(scannerRecord);
        }
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
