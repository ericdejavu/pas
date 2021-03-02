package com.homework.pas.model.bean;

import lombok.Data;

@Data
public class GoodsReport {
    private String merchandise;
    private String inventoryQuantity;
    private String inventoryAmount;
    private String salesQuantity;
    private String salesAmount;
    private String profit;
}
