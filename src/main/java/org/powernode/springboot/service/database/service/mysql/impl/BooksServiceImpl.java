package org.powernode.springboot.service.database.service.mysql.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.database.Books;
import org.powernode.springboot.bean.vo.BooksStorage;
import org.powernode.springboot.bean.vo.Chart;
import org.powernode.springboot.exception.SourceRepeatError;
import org.powernode.springboot.mapper.database.BooksMapper;
import org.powernode.springboot.mapper.database.BorrowTimeMapper;
import org.powernode.springboot.service.database.service.mysql.BookService;
import org.powernode.springboot.service.database.service.mysql.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class BooksServiceImpl implements BooksService {
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private BorrowTimeMapper borrowTimeMapper;
    @Autowired
    private BookService bookService;
    @Override
    @Transactional
    @TransactionFail
    public int insertBook(Books book) {
        String name=book.getName();
        String author=book.getAuthor();
        if(hasBook(name,author))
            throw new SourceRepeatError("作者为"+book.getAuthor()+",书名是"+book.getName()+"的书籍已经在库中");
        int res=booksMapper.insertBook(book);
            if(res<=0){
                return -1;
            }
        return borrowTimeMapper.insertBorrow(book.getId());
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteBook(String name,String author) {
        if(booksMapper.hasBook(name,author)<=0)
            throw new SourceRepeatError("当前馆藏中没有书名是"+name+",作者是"+author+"的书籍，请刷新检查");
        int id=booksMapper.getBookId(name,author);
        if(booksMapper.deleteBook(id)<=0)
            return -1;
        if(borrowTimeMapper.deleteBorrow(id)<=0){
            return -1;
        }
        return bookService.deleteAllBooksBook(id);
    }

    @Override
    @Transactional
    @TransactionFail
    public int updateBook(int id, String state) {
        return booksMapper.updateState(id,state);
    }

    @Override
    @Transactional
    public int getBookId(String name, String author) {
        return booksMapper.getBookId(name,author);
    }

    @Override
    @Transactional
    public Books selectBook(long id) {
        return booksMapper.selectBook(id);
    }

    @Override
    @Transactional
    public List<Books> selectAllBooks() {
        return booksMapper.selectAllBooks();
    }

    @Override
    @Transactional
    public List<Books> selectAllBooksByAuthor(String author) {
        return booksMapper.selectAllBooksByAuthor(author);
    }

    @Override
    @Transactional
    public List<Books> selectAllBooksByTitle(String title) {
        return booksMapper.selectAllBooksByTitle(title);
    }

    @Override
    @Transactional
    public List<BooksStorage> selectAllBooksInCondition(String name, String author, String type, String state, String[] heads) {
        List<Books> booksList=booksMapper.selectAllBooksInThisCondition(name,author,type,state,heads);
        List<BooksStorage> booksStorageList=new ArrayList<>();
        for(Books book:booksList){
            BooksStorage showBooksStorage=new BooksStorage(book);
            long time=borrowTimeMapper.searchBorrow(book.getId());
            showBooksStorage.setTime(time);
            booksStorageList.add(showBooksStorage);
        }
        return booksStorageList;
    }

    @Override
    @Transactional
    public boolean hasBook(String name, String author) {
        return booksMapper.hasBook(name,author)>0;
    }

    @Override
    @Transactional
    public List<Chart> getCharts() {
        return booksMapper.selectChart();
    }
}
