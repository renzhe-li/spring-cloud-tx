package com.demo.tx.common.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transfer {
    private long id;
    private long fromUser;
    private long toUser;
    private BigDecimal amount;
    private String status;
    private long createdTime;

}
