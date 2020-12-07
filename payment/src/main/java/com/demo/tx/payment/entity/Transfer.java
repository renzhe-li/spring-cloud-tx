package com.demo.tx.payment.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Transfer {
    private long id;
    private long from;
    private long to;
    private BigDecimal amount;
    private TransferStatus status;
    private long createdTime;

}
