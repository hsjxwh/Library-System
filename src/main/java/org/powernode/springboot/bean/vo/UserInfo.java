package org.powernode.springboot.bean.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfo {
    long id;
    String name;
    String num;
    double balance;
    int hasService;
    int borrow;

    public UserInfo() {
    }

    public UserInfo(long id, String name, String num, double balance, int hasService, int borrow) {
        this.id = id;
        this.name = name;
        this.num = num;
        this.balance = balance;
        this.hasService = hasService;
        this.borrow = borrow;
    }
}
