package com.demo.tx.payment.service.impl;

import com.demo.tx.payment.dao.UserDao;
import com.demo.tx.payment.entity.User;
import com.demo.tx.payment.service.UserService;
import com.demo.tx.payment.util.JsonUtils;
import com.demo.tx.payment.util.SecureRandomUtils;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_PREFIX = "USER:";

    @Autowired
    private UserDao userDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public User getByUsername(String username) {
        final String json = stringRedisTemplate.opsForValue().get(USER_PREFIX + username);

        if (!Strings.isNullOrEmpty(json)) {
            return JsonUtils.fromJson(json, User.class);
        }

        final User userFromDB = userDao.queryUserByName(username);
        if (userFromDB != null) {
            stringRedisTemplate.opsForValue().set(USER_PREFIX + username, JsonUtils.toJson(userFromDB),
                    SecureRandomUtils.nextInt(3600, 1200), TimeUnit.SECONDS);
        }

        return userFromDB;
    }

    @Override
    public User getById(int id) {
        return userDao.queryUserById(id);
    }

    @Override
    public User register(User user) {
        final User byUsername = getByUsername(user.getName());
        if (byUsername != null) {
            throw new IllegalArgumentException("Already Exist with same username!");
        }

        final User byPhone = userDao.queryUserByPhone(user.getPhone());
        if (byPhone != null) {
            throw new IllegalArgumentException("You have a account with the same phone!");
        }

        userDao.insertUser(user);

        return getByUsername(user.getName());
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            return null;
        }

        userDao.updateUser(user);

        return userDao.queryUserByName(user.getName());
    }

    @Override
    public boolean deleteByUsername(String username) {
        return userDao.deleteUserByName(username) > 0;
    }

}
