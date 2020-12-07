package com.demo.tx.payment.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private long id;
    private String txId;
    private TransactionType txType;
    private BigDecimal amount;
    private long userId;
    private String username;
    private long createdTime;

}
