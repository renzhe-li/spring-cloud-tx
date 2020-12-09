package com.demo.tx.common.dao;

import com.demo.tx.common.dao.mapper.UserMapper;
import com.demo.tx.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDao {

    @Autowired
    private UserMapper userMapper;

    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    public User queryUserByName(String username) {

        return userMapper.queryUserByName(username);
    }

    public User queryUserById(int id) {

        return userMapper.queryUserById(id);
    }

    public User queryUserByPhone(String phone) {
        return userMapper.queryUserByPhone(phone);
    }

    public void insertUser(User user) {
        userMapper.insertUser(user);
    }

    public int deleteUserByName(String username) {
        return userMapper.deleteUserByName(username);
    }

}
