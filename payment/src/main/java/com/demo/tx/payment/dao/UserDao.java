package com.demo.tx.payment.dao;

import com.demo.tx.payment.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    User queryUserByName(String username);

    User queryUserById(int id);

    User queryUserByPhone(String phone);

    User queryUserByMaxId();

    int insertUser(User user);

    int updateUser(User user);

    int deleteUserByName(String username);

    int deleteUserById(long id);

}
