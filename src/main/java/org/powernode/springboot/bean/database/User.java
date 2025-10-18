package org.powernode.springboot.bean.database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    long id;
    String name;
    String password;
    //手机号
    String num;
    //余额
    double balance;
    //服务模式
    //为0无服务，为1则初始押金为50，为22则初始押金为100
    int hasService;
    //已经借阅的书籍的数量
    int borrow;

    public User(long id, String name, String password, String num, double balance, int hasService,int borrow) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.num = num;
        this.balance = balance;
        this.hasService = hasService;
        this.borrow = borrow;
    }

    public User(String name, String password, String num) {
        this.name = name;
        this.password = password;
        this.num = num;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", num='" + num + '\'' +
                ", balance=" + balance +
                ", hasService=" + hasService +
                '}';
    }
}
