package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.mysql.PurchaseBooks;

import java.util.List;

public interface PurchaseBooksMapper {
    int insertRecord(@Param("record") PurchaseBooks record);
    int deleteRecord(@Param("name") String name, @Param("author") String author);
    List<PurchaseBooks> selectAllRecords();
}
