package org.powernode.springboot.service.database.service;

import org.powernode.springboot.bean.database.PurchaseBooks;

import java.util.List;

public interface PurchaseBooksService {
    int insertRecord(PurchaseBooks purchaseBooks);
    int deleteRecord(String author, String name);
    List<PurchaseBooks> selectAllRecords();
}
