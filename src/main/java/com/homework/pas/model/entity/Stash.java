package com.homework.pas.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Stash {
    private Long id;
    private String qrcode;
    private BigDecimal pay;
    private Integer amount;
    private Integer consume;
    private Boolean isSoldOut;
    private String optDate;
    private Date updateTime;
    private Date createTime;
}
