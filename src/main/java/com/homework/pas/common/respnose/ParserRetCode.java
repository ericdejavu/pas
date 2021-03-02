package com.homework.pas.common.respnose;

public enum  ParserRetCode implements ResponseCode {
    PARSER_ERROR_PARAMS(-100, " parse in params incorrect"),
    PARSER_OPT_OK(0, " parse success"),
    PARSER_ERROR_RECORD_CANT_BE_EMPTY(-1, " cause by record can't be empty"),
    PARSER_ERROR_RECORD_FORMAT_INCORRECT(-2, " cause by incorrect record format"),
    PARSER_ERROR_DATE_FORMAT_INCORRECT(-3, " cause by incorrect date format"),
    PARSER_ERROR_INVALID_COMMAND(-4, " cause by invalid command"),
    PARSER_ERROR_DUPLICATE_QRCODE(-5, " cause by invalid command"),
    PARSER_ERROR_PRICE_FORMAT_INCORRECT(-6, " cause by invalid command"),
    PARSER_ERROR_NAME_FORMAT_INCORRECT(-6, " name is too long"),
    PARSER_ERROR_NO_QRCODE_FOUND(-7, " Did not found merchandise with barcode euqals"),
    PARSER_ERROR_QRCODE_EMPTY(-8, " cause by qrcode empty"),
    PARSER_ERROR_AMOUNT_FORMAT_INCORRECT(-9, " case by amount format incorrect");

    private int code;
    private String msg;
    ParserRetCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
        RetCodeDispatcher.parseRetCodeMap.put(code, this);
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
