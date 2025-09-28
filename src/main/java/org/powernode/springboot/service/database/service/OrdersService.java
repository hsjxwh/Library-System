package org.powernode.springboot.service.database.service;

import org.powernode.springboot.bean.database.Orders;
import org.powernode.springboot.bean.vo.ManagerShowOrders;
import org.powernode.springboot.bean.vo.UserShowOrders;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersService {
    int insertOrders(long userId,long managerId,double money,int serviceType,String purpose, LocalDateTime time );
    int deleteOrders(long id);
    List<ManagerShowOrders> getAllOrders();
    List<ManagerShowOrders> managerGetSomeoneOrders(long userId);
    List<UserShowOrders> userGetAllOrders(long userId);
    ManagerShowOrders managerGetOrdersByOrder(long id);
    UserShowOrders userGetOrdersById(long id);
}
