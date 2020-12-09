package com.demo.tx.common.dao.mapper;

import com.demo.tx.common.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User queryUserByName(String username);

    User queryUserById(int id);

    User queryUserByPhone(String phone);

    User queryUserByMaxId();

    int insertUser(User user);

    int updateUser(User user);

    int deleteUserByName(String username);

    int deleteUserById(long id);

}
