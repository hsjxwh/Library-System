package org.powernode.springboot.bean.mysql;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
//借阅记录
@Getter
@Setter
public class Book {
    long bookId;
    long userId;
    //处理借书业务的管理员编号
    long managerId1;
    //处理还书业务的管理员编号
    long managerId2;
    //借书时间
    LocalDateTime time;
    //期望还书时间
    LocalDateTime expectedReturnTime;
    //还书时间
    LocalDateTime returnTime;
    //借阅记录编号（主键）
    long id;
    //是都已经归还书籍
    boolean restitution;
    //押金
    double deposit;
    //续借次数
    int renewTime;
    //假如扣除了押金，此为交易的订单编号
    long orderId;

    public Book(long bookId, long userId, long managerId1, LocalDateTime time, LocalDateTime expectedReturnTime, LocalDateTime returnTime,long managerId2, long id, boolean restitution, double deposit, int renewTime) {
        this.bookId = bookId;
        this.userId = userId;
        this.managerId1 = managerId1;
        this.time = time;
        this.managerId2 = managerId2;
        this.expectedReturnTime = expectedReturnTime;
        this.id = id;
        this.returnTime = returnTime;
        this.restitution = restitution;
        this.deposit = deposit;
        this.renewTime = renewTime;
    }

    public Book(long bookId, long userId, long managerId1, LocalDateTime time, LocalDateTime expectedReturnTime, LocalDateTime returnTime,long managerId2, long id, boolean restitution, double deposit, int renewTime,long orderId) {
        this.bookId = bookId;
        this.userId = userId;
        this.managerId1 = managerId1;
        this.time = time;
        this.managerId2 = managerId2;
        this.expectedReturnTime = expectedReturnTime;
        this.id = id;
        this.returnTime = returnTime;
        this.restitution = restitution;
        this.deposit = deposit;
        this.renewTime = renewTime;
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", userId=" + userId +
                ", managerId1=" + managerId1 +
                ", managerId2=" + managerId2 +
                ", time=" + time +
                ", expectedReturnTime=" + expectedReturnTime +
                ", returnTime=" + returnTime +
                ", id=" + id +
                ", restitution=" + restitution +
                ", deposit=" + deposit+
                '}';
    }
}
