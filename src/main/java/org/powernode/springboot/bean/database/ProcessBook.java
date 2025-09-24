package org.powernode.springboot.bean.database;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
//待审核的用户提交的书籍
public class ProcessBook {
    String name;
    String author;
    long id;
    String description;
    String type;
    //提交申请日期
    LocalDateTime date;
    String time;

    public ProcessBook() {
    }

    public ProcessBook(String name, String author, long id, String description, String type, LocalDateTime date) {
        this.name = name;
        this.author = author;
        this.id = id;
        this.description = description;
        this.type = type;
        this.date = date;
    }
}
