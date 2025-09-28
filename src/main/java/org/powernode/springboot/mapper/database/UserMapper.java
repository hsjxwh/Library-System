package org.powernode.springboot.mapper.database;

import org.apache.ibatis.annotations.Param;
import org.powernode.springboot.bean.database.User;
import org.powernode.springboot.bean.vo.UserInfo;

import java.util.List;

public interface UserMapper {
    int insertUser(User user);
    int deleteUser(long id);
    int updateUser(User user);
    List<User> selectAll();
    User selectById(long id);
    int hasUser(long id);
    String selectPassword(long id);
    int getBorrow(long id);
    int getServiceType(long id);
    int updateBorrow(@Param("id") long id, @Param("borrow") int borrow);
    int updateServiceType(@Param("id") long id, @Param("serviceType") int serviceType);
    int updateBalance(@Param("id") long id, @Param("balance") double balance);
    UserInfo getUserInfo(long id);
    double getBalance(long id);
}
