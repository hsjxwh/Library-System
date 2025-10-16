package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.database.Orders;
import org.powernode.springboot.bean.vo.ManagerShowOrders;
import org.powernode.springboot.bean.vo.UserShowOrders;

import java.time.LocalDateTime;
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
