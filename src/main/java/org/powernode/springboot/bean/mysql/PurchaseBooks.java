package org.powernode.springboot.bean.mysql;

import lombok.Getter;
import lombok.Setter;

//待采购的书籍
@Getter
@Setter
public class PurchaseBooks {
    String name;
    String author;
    String description;
    String head;
    String type;

    public PurchaseBooks() {
    }

    public PurchaseBooks(String name, String author, String description, String head, String type) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.head = head;
        this.type = type;
    }
}
