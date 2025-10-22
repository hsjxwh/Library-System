package org.powernode.springboot.mapper.database;

import org.powernode.springboot.bean.mysql.Manager;

import java.util.List;

public interface ManagerMapper {
    //插入新管理员
    int insertManager(Manager manager);
    //删除某管理员
    int deleteManager(long id);
    //更新管理员的信息
    int updateManager(Manager manager);
    Manager selectManager(long id);
    List<Manager> selectAllManager();
    int hasManager(long id);
    String selectPassword(long id);
}
