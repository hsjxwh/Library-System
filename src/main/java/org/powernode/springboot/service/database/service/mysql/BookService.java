package org.powernode.springboot.service.database.service.mysql;

import org.powernode.springboot.bean.vo.ShowBook;

import java.time.LocalDateTime;
import java.util.List;

public interface BookService {
    int insertBook(long bookId,long userId,long managerId1,LocalDateTime time,LocalDateTime expectedReturnTime);
    int deleteBook(long id);
    int deleteAllBooksBook(long bookId);
    int updateBook(long id, long bookId,long userId,LocalDateTime returnTime,LocalDateTime expectedReturnTime,double refund,long managerId);
    boolean hasThisBook(long id);
    //获取某本数的借阅次数
    long getBooksBookNum(long id);
    List<ShowBook> selectAllBook();
    List<ShowBook> selectSomeoneBook(long userId);
    List<ShowBook> selectAllBook(String name, String author, long id);
    boolean hasOrder(long id);
    long getBookOrderId(long id);
}
