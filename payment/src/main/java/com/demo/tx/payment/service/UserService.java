package com.demo.tx.payment.service;


import com.demo.tx.payment.entity.User;

public interface UserService {

    User getByUsername(String username);

    User getById(int id);

    User register(User user);

    User updateUser(User user);

    boolean deleteByUsername(String username);

}
