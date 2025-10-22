package org.powernode.springboot.mapper.database;

import org.powernode.springboot.bean.mysql.Orders;
import org.powernode.springboot.bean.vo.ManagerShowOrders;
import org.powernode.springboot.bean.vo.UserShowOrders;

import java.util.List;

public interface OrdersMapper {
    int insertOrders(Orders order);
    int deleteOrders(long id);
    List<ManagerShowOrders> getAllOrders();
    List<ManagerShowOrders> managerGetSomeoneOrders(long userId);
    List<UserShowOrders> userGetAllOrders(long userId);
    ManagerShowOrders mangerGetOrdersByOrderId(long id);
    UserShowOrders userGetOrdersById(long id);
}
