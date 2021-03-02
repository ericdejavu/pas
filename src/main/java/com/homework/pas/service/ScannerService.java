package com.homework.pas.service;

import com.homework.pas.common.respnose.ParserRetCode;
import com.homework.pas.common.respnose.ResponseCode;
import com.homework.pas.common.respnose.ServletRetCode;
import com.homework.pas.common.util.DiaryRecordParser;
import com.homework.pas.mapper.GoodsMapper;
import com.homework.pas.mapper.ScannerRecordMapper;
import com.homework.pas.mapper.StashMapper;
import com.homework.pas.model.bean.GoodsReport;
import com.homework.pas.model.bean.response.BaseResponseBody;
import com.homework.pas.model.entity.Goods;
import com.homework.pas.model.entity.ScannerRecord;
import com.homework.pas.model.entity.Stash;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");


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

        List<Stash> stashes = stashMapper.selectByQrcodeAndOrderByOptDate(scannerRecord.getQrcode(), false);
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
        List<Goods> goodsList = goodsMapper.selectAll();
        List<GoodsReport> reports = new ArrayList<>();
        for (Goods goods  : goodsList) {
            GoodsReport report = new GoodsReport();
            int inventoryQuantity = 0;
            BigDecimal inventoryAmount = new BigDecimal(0.0);
            int salesQuantity = 0;
            BigDecimal salesAmount = new BigDecimal(0.0);
            BigDecimal profit = new BigDecimal(0.0);

            report.setMerchandise(goods.getName());
            List<Stash> stashes = stashMapper.selectByQrcodeAndOrderByOptDate(goods.getQrcode(), true);
            for (Stash stash : stashes) {
                if (stash.getIsSoldOut()) {
                    salesQuantity += stash.getConsume();
                    salesAmount = salesAmount.add(new BigDecimal(stash.getConsume()).multiply(goods.getPrice()));
                } else {
                    if (stash.getConsume() > 0) {
                        salesQuantity += stash.getConsume();
                        salesAmount = salesAmount.add(new BigDecimal(stash.getConsume()).multiply(goods.getPrice()));
                    }
                    inventoryQuantity += stash.getAmount() - stash.getConsume();
                    inventoryAmount = inventoryAmount.add(new BigDecimal(stash.getAmount() - stash.getConsume()).multiply(stash.getPay()));
                }
            }
            report.setInventoryQuantity(String.valueOf(inventoryQuantity));
            report.setInventoryAmount(decimalFormat.format(inventoryAmount));
            report.setSalesQuantity(String.valueOf(salesQuantity));
            report.setSalesAmount(decimalFormat.format(salesAmount));
            report.setProfit(decimalFormat.format(profit));
            reports.add(report);
        }
        return new BaseResponseBody(ServletRetCode.SERVER_OPT_OK, reports);
    }

    @Deprecated
    public BaseResponseBody getValidRecordLogs() {
        // data : List<ScannerRecord>
        return null;
    }
}
