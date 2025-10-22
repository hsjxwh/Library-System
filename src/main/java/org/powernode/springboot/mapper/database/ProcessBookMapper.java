package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.mysql.ProcessBook;

import java.time.LocalDateTime;
import java.util.List;

public interface ProcessBookMapper {
    int insertRecord(@Param("record") ProcessBook record);
    int deleteRecord(@Param("id") long id,@Param("date") LocalDateTime time);
    List<ProcessBook> selectAllRecords();
}
