package com.homework.pas.mapper;

import com.homework.pas.model.entity.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsMapper {
    Goods getIfExist(@Param("qrcode") String qrcode);
    void save(@Param("goods") Goods goods);
}
