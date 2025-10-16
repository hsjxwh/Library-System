package org.powernode.springboot.service.excel.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.Getter;
import org.powernode.springboot.bean.database.Books;
import org.powernode.springboot.bean.vo.ImportBooksByExcelRes;
import org.powernode.springboot.service.database.service.mysql.BooksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
public class BooksExcelListener implements ReadListener<Books> {
    private static final Logger logger = LoggerFactory.getLogger(BooksExcelListener.class);
    private final BooksService booksService;
    ArrayList<Books> booksList=new ArrayList<>();
    @Getter
    ImportBooksByExcelRes res=new ImportBooksByExcelRes();
    public BooksExcelListener(BooksService booksService) {
        this.booksService = booksService;
    }

    @Override
    public void invoke(Books books, AnalysisContext analysisContext) {
        books.setState("在馆");
        booksList.add(books);
        if(booksList.size()>=100){
            saveAllBooks();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(!booksList.isEmpty())
            saveAllBooks();
        logger.info("本次数据已经导入完毕,一共{}条数据,成功了{}条，失败了{}条",res.getTotalCount(),res.getSuccessCount(),res.getFailCount());
    }

    private void saveAllBooks(){
        for(Books book:booksList){
            res.addCount();
            logger.info("正在导入书本数据："+book);
            try {
                if(booksService.insertBook(book)>0) {
                    res.addSuccess();
                }
                else{
                    res.addFail();
                    res.addSentences("导入名字为"+book.getName()+",作者为"+book.getAuthor()+"的书籍失败");
                }
            } catch (Exception e) {
                res.addFail();
                res.addSentences("导入名字为"+book.getName()+",作者为"+book.getAuthor()+"的书籍失败");
            }
        }
        booksList.clear();
    }

}
