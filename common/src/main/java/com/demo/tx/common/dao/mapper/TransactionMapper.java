package com.demo.tx.common.dao.mapper;

import com.demo.tx.common.entity.Transaction;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransactionMapper {

    Transaction queryTransactionById(int id);

    List<Transaction> queryTransactionsByUserId(int userId);

    int insertTransaction(Transaction transaction);

}
