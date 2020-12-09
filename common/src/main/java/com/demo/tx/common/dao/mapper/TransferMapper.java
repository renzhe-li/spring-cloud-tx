package com.demo.tx.common.dao.mapper;

import com.demo.tx.common.entity.Transfer;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TransferMapper {

    Transfer queryTransferById(long id);

    List<Transfer> queryTransfersByUserId(long userId);

    int insertTransfer(Transfer transfer);

    int updateTransfer(Transfer transfer);

}
