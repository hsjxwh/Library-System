package org.powernode.springboot.service.database.service.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.database.Orders;
import org.powernode.springboot.bean.vo.ManagerShowOrders;
import org.powernode.springboot.bean.vo.UserShowOrders;
import org.powernode.springboot.mapper.database.OrdersMapper;
import org.powernode.springboot.mapper.database.UserMapper;
import org.powernode.springboot.service.database.service.OrdersService;
import org.powernode.springboot.service.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Override
    @Transactional
    @TransactionFail
    public int insertOrders(long userId, long managerId, double money, int serviceType, String purpose, LocalDateTime time) {
        int res= ordersMapper.insertOrders(userId, managerId, money,purpose,time);
        if(res<0){
            return -1;
        }
        res=userService.updateBalance(userId,money);
        if(res<0){
            return -1;
        }
        if(serviceType==1||serviceType==2){
            res= userMapper.updateServiceType(userId,serviceType);
        }
        return res;
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteOrders(long id) {
        return ordersMapper.deleteOrders(id);
    }

    @Override
    @Transactional
    public List<ManagerShowOrders> getAllOrders() {
        return ordersMapper.getAllOrders();
    }

    @Override
    @Transactional
    public List<ManagerShowOrders> managerGetSomeoneOrders(long userId) {
        return ordersMapper.managerGetSomeoneOrders(userId);
    }

    @Override
    @Transactional
    public List<UserShowOrders> userGetAllOrders(long userId) {
        return ordersMapper.userGetAllOrders(userId);
    }

    @Override
    @Transactional
    public ManagerShowOrders managerGetOrdersByOrder(long id) {
        return ordersMapper.mangerGetOrdersByOrderId(id);
    }

    @Override
    @Transactional
    public UserShowOrders userGetOrdersById(long id) {
        return ordersMapper.userGetOrdersById(id);
    }
}
