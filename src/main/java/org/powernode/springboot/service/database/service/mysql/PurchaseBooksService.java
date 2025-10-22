package org.powernode.springboot.service.database.service.mysql;

import org.powernode.springboot.bean.mysql.PurchaseBooks;

import java.util.List;

public interface PurchaseBooksService {
    int insertRecord(PurchaseBooks purchaseBooks);
    int deleteRecord(String author, String name);
    List<PurchaseBooks> selectAllRecords();
}
