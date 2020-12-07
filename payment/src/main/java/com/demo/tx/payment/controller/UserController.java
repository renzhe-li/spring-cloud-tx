package com.demo.tx.payment.controller;

import com.demo.tx.payment.controller.advice.ResponseResult;
import com.demo.tx.payment.entity.User;
import com.demo.tx.payment.service.UserService;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@ResponseResult
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public User getUserByUsername(@RequestParam(name = "id", defaultValue = "-1") int id,
                                  @RequestParam(name = "name", defaultValue = "") String username) {
        if (id >= 0) {
            return userService.getById(id);
        }

        if (!username.isEmpty()) {
            return userService.getByUsername(username);
        }

        throw new IllegalArgumentException("Id or Username should be provided!");
    }

    @PostMapping
    public User register(@RequestBody User user) {
        if (user == null) {
            throw new IllegalArgumentException("User should be provided!");
        }
        if (Strings.isNullOrEmpty(user.getName())) {
            throw new IllegalArgumentException("username should be not null!");
        }
        if (Strings.isNullOrEmpty(user.getPassword())) {
            throw new IllegalArgumentException("password should be not null!");
        }
        if (Strings.isNullOrEmpty(user.getSex())) {
            throw new IllegalArgumentException("sex should be not null!");
        }

        user.setCreatedTime(System.currentTimeMillis());

        return userService.register(user);
    }

}
