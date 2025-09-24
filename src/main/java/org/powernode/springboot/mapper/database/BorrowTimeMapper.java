package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.database.BorrowTime;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface BorrowTimeMapper {
    int insertBorrow(long id);
    int deleteBorrow(long id);
    int updateBorrow(@Param("id") long id,@Param("time") long time);
    long searchBorrow(long id);
    List<BorrowTime> selectBorrowTime(long id);
}
