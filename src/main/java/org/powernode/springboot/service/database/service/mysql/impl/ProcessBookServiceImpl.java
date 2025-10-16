package org.powernode.springboot.service.database.service.mysql.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.database.ProcessBook;
import org.powernode.springboot.mapper.database.ProcessBookMapper;
import org.powernode.springboot.service.database.service.mysql.ProcessBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class ProcessBookServiceImpl implements ProcessBookService {
    @Autowired
    private ProcessBookMapper processBookMapper;
    @Override
    @Transactional
    @TransactionFail
    public int insertRecord(ProcessBook processBook) {
        return processBookMapper.insertRecord(processBook);
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteRecord(long id, LocalDateTime time) {
        return processBookMapper.deleteRecord(id,time);
    }

    @Override
    @Transactional
    public List<ProcessBook> selectAllRecords() {
        return processBookMapper.selectAllRecords();
    }
}
