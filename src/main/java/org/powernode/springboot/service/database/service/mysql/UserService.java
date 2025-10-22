package org.powernode.springboot.service.database.service.mysql;

import org.powernode.springboot.bean.mysql.User;
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
