package org.powernode.springboot.bean.database;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//订单记录
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orders {
    long id;
    long userId;
    long managerId;
    double money;
    boolean back;
    String purpose;
    LocalDateTime time;
    public Orders(long userId,long managerId,double money,String purpose,LocalDateTime time) {
        this.userId = userId;
        this.managerId = managerId;
        this.money = money;
        this.purpose = purpose;
        this.time = time;
    }
}
