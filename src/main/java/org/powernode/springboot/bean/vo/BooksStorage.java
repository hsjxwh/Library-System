package org.powernode.springboot.bean.vo;

import org.powernode.springboot.bean.database.Books;

import java.util.Base64;

public class BooksStorage {
    long id;
    String name;
    String author;
    String state;
    String description;
    String type;
    double price;
    long time;

    public BooksStorage(long id, String name, String author, String state, String description, String type, long time,double price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.state = state;
        this.description = description;
        this.type = type;
        this.time = time;
        this.price = price;
    }

    public BooksStorage(Books books) {
        this.id = books.getId();
        this.name = books.getName();
        this.author = books.getAuthor();
        this.state = books.getState();
        this.description = books.getDescription();
        this.type = books.getType();
        this.price = books.getPrice();
    }

    public BooksStorage() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "ShowBooksStorage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", state='" + state + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", price=" + price +
                ", time=" + time +
                '}';
    }
}
