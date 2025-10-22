package org.powernode.springboot.service.database.service.mysql;

import org.powernode.springboot.bean.mysql.Manager;

import java.util.List;

public interface ManagerService {
    int insertManager(Manager manager);
    int deleteManager(long id);
    int updateManager(Manager manager);
    boolean hasManager(long id);
    Manager getManagerById(long id);
    List<Manager> selectAllManager();
    boolean checkPassword(long id,String password);
}
