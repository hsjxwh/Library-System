package org.powernode.springboot.service.database.service;

import org.powernode.springboot.bean.database.User;
import org.powernode.springboot.bean.vo.UserInfo;

import java.util.List;

public interface UserService {
    int insertUser(User user);
    //更新基础信息
    int updateUser(User user);
    int updateBalance(long id,double balance);
    int deleteUser(long id);
    boolean hasUser(long id);
    List<User> getUsers();
    User getUserById(long id);
    boolean checkPassword(long id, String password);
    UserInfo getUserInfo(long id);
}
