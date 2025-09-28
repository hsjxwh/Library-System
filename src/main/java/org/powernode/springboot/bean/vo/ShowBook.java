package org.powernode.springboot.bean.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowBook {
    long id;
    long bookId;
    long userId;
    String name;
    String author;
    LocalDateTime time;
    LocalDateTime expectedReturnTime;
    LocalDateTime returnTime;
    long managerId1;
    boolean restitution;
    double deposit;
    long managerId2;
    int renewTime;
    long orderId=-1;

    public ShowBook(long id, long bookId, long userId, String name, String author, LocalDateTime time, LocalDateTime expectedReturnTime, LocalDateTime returnTime, long managerId1, boolean restitution, double deposit, long managerId2, int renewTime,long orderId) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.name = name;
        this.author = author;
        this.time = time;
        this.expectedReturnTime = expectedReturnTime;
        this.returnTime = returnTime;
        this.managerId1 = managerId1;
        this.restitution = restitution;
        this.deposit = deposit;
        this.managerId2 = managerId2;
        this.renewTime = renewTime;
        this.orderId = orderId;
    }

    public ShowBook() {
    }
}
