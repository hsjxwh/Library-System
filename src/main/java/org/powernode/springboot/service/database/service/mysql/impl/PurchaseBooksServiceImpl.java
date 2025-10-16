package org.powernode.springboot.service.database.service.mysql.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.database.PurchaseBooks;
import org.powernode.springboot.exception.SourceRepeatError;
import org.powernode.springboot.mapper.database.BooksMapper;
import org.powernode.springboot.mapper.database.PurchaseBooksMapper;
import org.powernode.springboot.service.database.service.mysql.PurchaseBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class PurchaseBooksServiceImpl implements PurchaseBooksService {
    @Autowired
    private PurchaseBooksMapper purchaseBooksMapper;
    @Autowired
    private BooksMapper booksMapper;
    @Override
    @Transactional
    @TransactionFail
    public int insertRecord(PurchaseBooks purchaseBooks) {
        String name=purchaseBooks.getName();
        String author=purchaseBooks.getAuthor();
        if(booksMapper.hasBook(name,author)>0){
            throw new SourceRepeatError("名字是"+name+"作者是"+author+"的书籍已经在书馆种");
        }
        return purchaseBooksMapper.insertRecord(purchaseBooks);
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteRecord(String author, String name) {
        return purchaseBooksMapper.deleteRecord(name,author);
    }

    @Override
    @Transactional
    public List<PurchaseBooks> selectAllRecords() {
        return purchaseBooksMapper.selectAllRecords();
    }
}
