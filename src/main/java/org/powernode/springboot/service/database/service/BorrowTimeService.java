package org.powernode.springboot.service.database.service;

import org.powernode.springboot.bean.database.BorrowTime;

import java.util.List;

public interface BorrowTimeService {
    int insertBorrow(long id);
    int deleteBorrow(long id);
    int updateBorrow(long id,long time);
    long searchBorrow(long id);
    List<BorrowTime> searchBorrowTime(long id);
}
