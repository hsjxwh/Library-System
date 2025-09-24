package org.powernode.springboot.service.database.service.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.database.Manager;
import org.powernode.springboot.mapper.database.ManagerMapper;
import org.powernode.springboot.service.database.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerServiceImpl implements ManagerService {
    @Autowired
    private ManagerMapper managerMapper;

    @Override
    @Transactional
    @TransactionFail
    public int insertManager(Manager manager) {
        return managerMapper.insertManager(manager);
    }
    @Override
    @Transactional
    @TransactionFail
    public int updateManager(Manager manager) {
        return managerMapper.updateManager(manager);
    }

    @Override
    @Transactional
    public boolean hasManager(long id) {
        return managerMapper.hasManager(id)>0;
    }

    @Override
    @Transactional
    public Manager getManagerById(long id) {
        return managerMapper.selectManager(id);
    }

    @Override
    @Transactional
    public boolean checkPassword(long id,String password) {
        if(hasManager(id))
            return password!=null&&!password.isEmpty()&&password.equals(managerMapper.selectPassword(id));
        return false;
    }
    @Override
    @Transactional
    @TransactionFail
    public int deleteManager(long id) {
        return managerMapper.deleteManager(id);
    }
    @Override
    @Transactional
    public List<Manager> selectAllManager() {
        return managerMapper.selectAllManager();
    }
}
