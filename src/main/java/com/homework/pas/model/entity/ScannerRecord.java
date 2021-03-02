package com.homework.pas.model.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ScannerRecord {
    private Long id;
    private String optDate;
    private String optCmd;
    private String qrcode;
    private String param1;
    private String param2;
    private Date updateTime;
    private Date createTime;
}
