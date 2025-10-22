package org.powernode.springboot.bean.mysql;

import com.alibaba.excel.annotation.ExcelProperty;

public class Books {
    long id;
    @ExcelProperty(value = "书名", index = 0)
    String name;
    @ExcelProperty(value = "作者",index = 1)
    String author;
    String state;
    @ExcelProperty(value = "描述",index = 3)
    String description;
    @ExcelProperty(value = "类型",index = 2)
    String type;
    @ExcelProperty(value = "价格",index = 5)
    double price;
    //首字母
    @ExcelProperty(value = "书名首字母",index = 4)
    String head;

    public Books(long id, double price, String type, String description, String state, String author, String name,String head) {
        this.id = id;
        this.price = price;
        this.type = type;
        this.description = description;
        this.state = state;
        this.author = author;
        this.name = name;
        this.head = head;
    }

    public Books() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", head=" + head +
                '}';
    }
}
