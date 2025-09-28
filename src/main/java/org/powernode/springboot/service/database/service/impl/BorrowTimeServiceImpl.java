package org.powernode.springboot.service.database.service.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.database.BorrowTime;
import org.powernode.springboot.mapper.database.BorrowTimeMapper;
import org.powernode.springboot.service.database.service.BorrowTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class BorrowTimeServiceImpl implements BorrowTimeService {
    @Autowired
    private BorrowTimeMapper borrowTimeMapper;
    @Override
    @Transactional
    @TransactionFail
    public int insertBorrow(long id) {
        return borrowTimeMapper.insertBorrow(id);
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteBorrow(long id) {
        return borrowTimeMapper.deleteBorrow(id);
    }

    @Override
    @Transactional
    @TransactionFail
    public int updateBorrow(long id) {
        return borrowTimeMapper.updateBorrow(id, searchBorrow(id)+1);
    }

    @Override
    @Transactional
    public long searchBorrow(long id) {
        return borrowTimeMapper.searchBorrow(id);
    }

    @Override
    @Transactional
    public List<BorrowTime> searchBorrowTime(long id) {
        return borrowTimeMapper.selectBorrowTime(id);
    }
}
