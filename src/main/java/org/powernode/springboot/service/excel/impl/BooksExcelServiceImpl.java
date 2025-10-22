package org.powernode.springboot.service.excel.impl;

import com.alibaba.excel.EasyExcel;
import org.powernode.springboot.bean.mysql.Books;
import org.powernode.springboot.bean.vo.ImportBooksByExcelRes;
import org.powernode.springboot.service.database.service.mysql.BooksService;
import org.powernode.springboot.service.excel.BooksExcelService;
import org.powernode.springboot.service.excel.listener.BooksExcelListener;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class BooksExcelServiceImpl implements BooksExcelService {
    private final BooksService booksService;
    BooksExcelServiceImpl(BooksService booksService){
        this.booksService = booksService;
    }
    @Override
    public ImportBooksByExcelRes importBooksFromExcel(InputStream file) {
        BooksExcelListener listener=new BooksExcelListener(booksService);
        try {
            EasyExcel.read(file, Books.class,listener).sheet().doRead();
        } catch (Exception e) {
            ImportBooksByExcelRes excelRes=new ImportBooksByExcelRes();
            excelRes.addSentences("文件处理失败:"+e.getMessage());
            return excelRes;
        }
        return listener.getRes();
    }
}
