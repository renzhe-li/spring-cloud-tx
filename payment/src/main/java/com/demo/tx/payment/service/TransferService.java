package com.demo.tx.payment.service;

import com.demo.tx.common.entity.Transfer;

import java.util.List;

public interface TransferService {

    Transfer createTransfer(Transfer transfer);

    Transfer getTransfer(long transferId);

    List<Transfer> listTransfers(long userId);

}
