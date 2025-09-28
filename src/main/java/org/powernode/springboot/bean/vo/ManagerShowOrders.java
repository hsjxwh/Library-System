package org.powernode.springboot.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManagerShowOrders {
    long id;
    long managerId;
    double money;
    LocalDateTime time;
    String purpose;
    long bookId;
    String name;
    String author;
    boolean back;
    long userId;
}
