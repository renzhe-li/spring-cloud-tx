package com.demo.tx.payment.controller;

import com.demo.tx.payment.entity.Transfer;
import com.demo.tx.payment.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private TransferService transferService;

    @GetMapping("/transfer")
    public Transfer getTransfer(@RequestParam int transferId) {
        return transferService.getTransfer(transferId);
    }

    @GetMapping("/transfers")
    public List<Transfer> getTransfers(@RequestParam int userId) {
        return transferService.listTransfers(userId);
    }

    @PostMapping("/transfer")
    public Transfer submitTransfer(@RequestBody Transfer transfer) {
        transfer.setCreatedTime(System.currentTimeMillis());
        return transferService.createTransfer(transfer);
    }

}
