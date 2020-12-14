package com.demo.tx.payment.dao;

import com.demo.tx.payment.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionDao {

    Transaction queryTransactionById(int id);

    List<Transaction> queryTransactionsByUserId(int userId);

    int insertTransaction(Transaction transaction);

}
