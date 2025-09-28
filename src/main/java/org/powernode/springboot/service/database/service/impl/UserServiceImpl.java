package org.powernode.springboot.service.database.service.impl;

import org.powernode.springboot.annotation.TransactionFail;
import org.powernode.springboot.bean.database.User;
import org.powernode.springboot.bean.vo.UserInfo;
import org.powernode.springboot.mapper.database.ScoreMapper;
import org.powernode.springboot.mapper.database.UserMapper;
import org.powernode.springboot.service.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ScoreMapper scoreMapper;
    @Override
    @Transactional
    @TransactionFail
    public int insertUser(User user) {
        if(userMapper.insertUser(user)<=0)
            return -1;
        return scoreMapper.insertScore(user.getId());
    }

    @Override
    @Transactional
    @TransactionFail
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    @Transactional
    @TransactionFail
    public int updateBalance(long id, double balance) {
        double oldBalance = userMapper.getBalance(id);
        double newBalance = balance + oldBalance;
        return userMapper.updateBalance(id, newBalance);
    }

    @Override
    @Transactional
    @TransactionFail
    public int deleteUser(long id) {
        if(userMapper.deleteUser(id)<=0)
            return -1;
        return scoreMapper.deleteScore(id);
    }

    @Override
    @Transactional
    public boolean hasUser(long id) {
        return userMapper.hasUser(id)>0;
    }

    @Override
    @Transactional
    public List<User> getUsers() {
        return userMapper.selectAll();
    }

    @Override
    @Transactional
    public User getUserById(long id) {
        return userMapper.selectById(id);
    }

    @Override
    @Transactional
    public boolean checkPassword(long id, String password) {
        if(hasUser(id))
            return password!=null&&!password.isEmpty()&&password.equals(userMapper.selectPassword(id));
        return false;
    }

    @Override
    @Transactional
    public UserInfo getUserInfo(long id) {
        return userMapper.getUserInfo(id);
    }


}
