package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.database.Book;
import org.powernode.springboot.bean.vo.ShowBook;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface BookMapper {
    //获取所有借阅记录
    List<ShowBook> selectAllBook();
    //获取某人的借阅记录
    List<ShowBook> selectSomeoneBook(long userId);
    //获取某本书的所有借阅记录
    List<Integer> selectAllBooksBook(long bookId);
    List<ShowBook> selectAllBookRecord(@Param("name") String name,@Param("author") String author,@Param("id") long id);
    int getRenewTime(@Param("id") long id);
    //增加借阅记录
    int insertBook(@Param("bookId") long bookId,@Param("useId") long userId,@Param("managerId1") long managerId1,@Param("time")LocalDateTime time,@Param("expectedReturnTime") LocalDateTime expectedReturnTime);
    //删除借阅记录
    int deleteBook(long id);
    //记录借阅记录的归还日期和退回押金
    int updateBook(@Param("id") long id, @Param("returnTime") LocalDateTime returnTime,@Param("expectedReturnTime") LocalDateTime expectedReturnTime,@Param("deposit") double deposit,@Param("managerId") long managerId);
    int updateReturnTime(@Param("id") long id, @Param("renewTime")int renewTime);
    int hasThisBook(long id);
    long getBooksBookNum(long bookId);
    int hasOrders(long id);
    long getBookOrderId(long id);
}
