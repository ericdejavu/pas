package com.homework.pas.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Goods {
    private Long id;
    private String qrcode;
    private String name;
    private BigDecimal price;
    private Date updateTime;
    private Date createTime;
}
