package org.powernode.springboot.service.database.service;

import org.powernode.springboot.bean.database.ProcessBook;

import java.time.LocalDateTime;
import java.util.List;

public interface ProcessBookService {
    int insertRecord(ProcessBook processBook);
    int deleteRecord(long id , LocalDateTime time);
    List<ProcessBook> selectAllRecords();
}
