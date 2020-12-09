package com.demo.tx.common.dao;

import com.demo.tx.common.dao.mapper.TransactionMapper;
import com.demo.tx.common.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionDao {

    @Autowired
    private TransactionMapper transactionMapper;

    public void insertTransaction(Transaction transaction) {
        transactionMapper.insertTransaction(transaction);
    }
}
