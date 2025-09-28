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
    LocalDateTime time;
}
