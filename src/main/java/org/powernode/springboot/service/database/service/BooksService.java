package org.powernode.springboot.service.database.service;

import org.powernode.springboot.bean.database.Books;
import org.powernode.springboot.bean.vo.BooksStorage;
import org.powernode.springboot.bean.vo.Chart;

import java.util.List;

public interface BooksService {
    int insertBook(Books book);
    int deleteBook(String name,String author);
    int updateBook(int id,String state);
    int getBookId(String name,String author);
    Books selectBook(long id);
    List<Books> selectAllBooks();
    List<Books> selectAllBooksByAuthor(String author);
    List<Books> selectAllBooksByTitle(String title);
    List<BooksStorage> selectAllBooksInCondition(String name,String author,String type,String state,String[] heads);
    boolean hasBook(String name,String author);
    //获取借阅排名
    List<Chart> getCharts();
}
