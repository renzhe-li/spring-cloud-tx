package com.demo.tx.payment.entity;

import com.demo.tx.payment.constant.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transaction {
    private long id;
    private long txId;
    private TransactionType txType;
    private BigDecimal amount;
    private long userId;
    private long createdTime;

}
