package com.demo.tx.payment.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class User {
    private long id;
    private String name;
    private String sex;
    private String phone;
    private String password;
    private BigDecimal balance;
    private long createdTime;

}
