package com.demo.tx.common.dao;

import com.demo.tx.common.dao.mapper.TransactionMapper;
import com.demo.tx.common.dao.mapper.TransferMapper;
import com.demo.tx.common.entity.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransferDao {

    @Autowired
    private TransferMapper transferMapper;

    public void insertTransfer(Transfer transfer) {
        transferMapper.insertTransfer(transfer);
    }

    public void updateTransfer(Transfer transfer) {
        transferMapper.updateTransfer(transfer);
    }

    public Transfer queryTransferById(long transferId) {
        return transferMapper.queryTransferById(transferId);
    }

    public List<Transfer> queryTransfersByUserId(long userId) {
        return transferMapper.queryTransfersByUserId(userId);
    }
}
