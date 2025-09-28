package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.database.Orders;
import org.powernode.springboot.bean.vo.ManagerShowOrders;
import org.powernode.springboot.bean.vo.UserShowOrders;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersMapper {
    int insertOrders(@Param("userId") long userId, @Param("managerId") long managerId, @Param("money") double money, @Param("purpose" ) String purpose, @Param("time") LocalDateTime time);
    int deleteOrders(long id);
    List<ManagerShowOrders> getAllOrders();
    List<ManagerShowOrders> managerGetSomeoneOrders(long userId);
    List<UserShowOrders> userGetAllOrders(long userId);
    ManagerShowOrders mangerGetOrdersByOrderId(long id);
    UserShowOrders userGetOrdersById(long id);
}
