package org.powernode.springboot.service.database.service.mysql;

import org.powernode.springboot.bean.mysql.ProcessBook;

import java.time.LocalDateTime;
import java.util.List;

public interface ProcessBookService {
    int insertRecord(ProcessBook processBook);
    int deleteRecord(long id , LocalDateTime time);
    List<ProcessBook> selectAllRecords();
}
