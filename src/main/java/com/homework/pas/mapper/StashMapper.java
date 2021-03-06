package com.homework.pas.mapper;

import com.homework.pas.model.entity.Stash;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StashMapper {
    void insert(@Param("stash") Stash stash);
    void update(@Param("stash") Stash stash);
    List<Stash> selectByQrcodeAndOrderByOptDate(@Param("qrcode") String qrcode, @Param("containsSoldOut") Boolean containsSoldOut);
    void deleteAllStash();
}
