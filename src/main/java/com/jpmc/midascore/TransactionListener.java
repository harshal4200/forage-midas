package com.jpmc.midascore;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.service.TransactionService;

@Component
public class TransactionListener {

    private final TransactionService transactionService;

    public TransactionListener(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void receiveTransaction(Transaction transaction) {
        System.out.println("Received Transaction: " + transaction);

        // Process transaction through service (database save + validation)
        boolean success = transactionService.processTransaction(transaction);

        if (success) {
            System.out.println("✅ Transaction processed: " + transaction.getAmount());
        } else {
            System.out.println("❌ Transaction rejected: " + transaction.getAmount());
        }
    }
}