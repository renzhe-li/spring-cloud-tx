package com.demo.tx.payment.dao;

import com.demo.tx.payment.entity.Transfer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferDao {

    Transfer queryTransferById(long id);

    List<Transfer> queryTransfersByUserId(long userId);

    int insertTransfer(Transfer transfer);

    int updateTransfer(Transfer transfer);

}
