package com.demo.tx.payment.service.impl;

import com.demo.tx.payment.constant.MessageType;
import com.demo.tx.payment.constant.TransactionType;
import com.demo.tx.payment.constant.TransferStatus;
import com.demo.tx.payment.dao.TransactionDao;
import com.demo.tx.payment.dao.TransferDao;
import com.demo.tx.payment.entity.Transaction;
import com.demo.tx.payment.entity.Transfer;
import com.demo.tx.payment.service.TransferService;
import com.demo.tx.payment.util.JsonUtils;
import com.demo.tx.payment.util.SecureRandomUtils;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class TransferServiceImpl implements TransferService {
    private static final String TRANSFER_PREFIX = "TRANSFER:";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private TransferDao transferDao;

    @Autowired
    private TransactionDao transactionDao;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Transactional
    @Override
    public Transfer createTransfer(Transfer transfer) {
        transfer.setStatus(TransferStatus.SEND_TO_MQ_N.name());
        transferDao.insertTransfer(transfer);

        final Transaction transaction = new Transaction();
        transaction.setTxId(transfer.getId());
        transaction.setTxType(TransactionType.TRANSFER_OUT);
        transaction.setUserId(transfer.getFromUser());
        transaction.setAmount(transfer.getAmount().negate());
        transactionDao.insertTransaction(transaction);

        kafkaTemplate.send("transfer", MessageType.TRANSFER.name(), transfer);

        transfer.setStatus(TransferStatus.SEND_TO_MQ_Y.name());
        transferDao.insertTransfer(transfer);

        return getTransfer(transfer.getId());
    }

    @Override
    public Transfer getTransfer(long transferId) {
        final String json = stringRedisTemplate.opsForValue().get(TRANSFER_PREFIX + transferId);
        if (!Strings.isNullOrEmpty(json)) {
            return JsonUtils.fromJson(json, Transfer.class);
        }

        final Transfer transfer = transferDao.queryTransferById(transferId);

        if (transfer != null) {
            stringRedisTemplate.opsForValue().set(TRANSFER_PREFIX + transferId, JsonUtils.toJson(transfer),
                    SecureRandomUtils.nextInt(3600, 1200), TimeUnit.SECONDS);
        }

        return transfer;
    }

    @Override
    public List<Transfer> listTransfers(long userId) {
        return transferDao.queryTransfersByUserId(userId);
    }

}
