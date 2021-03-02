package com.homework.pas.mapper;

import com.homework.pas.model.entity.ScannerRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ScannerRecordMapper {
    void insert(@Param("scannerRecord") ScannerRecord scannerRecord);
    List<ScannerRecord> selectAll();
}
