package org.powernode.springboot.service.excel.impl;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.ReadListener;
import org.powernode.springboot.bean.database.Books;
import org.powernode.springboot.bean.vo.ImportBooksByExcelRes;
import org.powernode.springboot.service.database.service.BooksService;
import org.powernode.springboot.service.excel.BooksExcelService;
import org.powernode.springboot.service.excel.listener.BooksExcelListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
