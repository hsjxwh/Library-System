package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.database.Books;
import org.powernode.springboot.bean.vo.Chart;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface BooksMapper {
    //插入书籍
    int insertBook(@Param("books") Books books);
    int deleteBook(long id);
    //根据书籍编号更新书籍的状态
    int updateState(@Param("id") long id,@Param("state") String state);
    //获取书籍的信息
    Books selectBook(long id);
    int hasBook(@Param("name") String name,@Param("author") String author);
    int getBookId(@Param("name")String name,@Param("author") String author);
    List<Books> selectAllBooks();
    //获取此作者所有书籍
    List<Books> selectAllBooksByAuthor(String author);
    //根据书名获取书籍
    List<Books> selectAllBooksByTitle(@Param("name") String title);
    List<Books> selectAllBooksInThisCondition(@Param("name") String name, @Param("author") String author,@Param("type") String type,@Param("state") String hasBorrow,@Param("heads") String[] heads);
    List<Chart> selectChart();
}
