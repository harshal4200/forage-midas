package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private final UserRecordRepository userRecordRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveService incentiveService;  // YEH ADD KARO

    public TransactionService(UserRecordRepository userRecordRepository,
                              TransactionRecordRepository transactionRecordRepository,
                              IncentiveService incentiveService) {  // YEH ADD KARO
        this.userRecordRepository = userRecordRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveService = incentiveService;  // YEH ADD KARO
    }

    @Transactional
    public boolean processTransaction(Transaction transaction) {
        // Validate sender exists
        UserRecord sender = userRecordRepository.findById(transaction.getSenderId()).orElse(null);
        if (sender == null) {
            System.out.println("Invalid sender ID: " + transaction.getSenderId());
            return false;
        }

        // Validate recipient exists
        UserRecord recipient = userRecordRepository.findById(transaction.getRecipientId()).orElse(null);
        if (recipient == null) {
            System.out.println("Invalid recipient ID: " + transaction.getRecipientId());
            return false;
        }

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance. Sender: " + sender.getBalance() + ", Required: " + transaction.getAmount());
            return false;
        }

        // YEH NAYA CODE ADD KARO - Incentive API call karo
        float incentiveAmount = incentiveService.getIncentiveAmount(transaction);
        System.out.println("Incentive received: " + incentiveAmount);

        // Process transaction (WITH INCENTIVE)
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount);  // YEH CHANGE KARO

        // Save updated balances
        userRecordRepository.save(sender);
        userRecordRepository.save(recipient);

        // Create and save transaction record WITH INCENTIVE
        TransactionRecord record = new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount);  // YEH CHANGE KARO
        transactionRecordRepository.save(record);

        System.out.println("Transaction processed: " + transaction.getAmount() + " + incentive: " + incentiveAmount + " from " + sender.getName() + " to " + recipient.getName());
        return true;
    }

    // Waldorf ka balance check karne ke liye helper method
    public float getWaldorfBalance() {
        UserRecord waldorf = userRecordRepository.findByName("waldorf").orElse(null);
        return waldorf != null ? waldorf.getBalance() : 0.0f;
    }

    // YEH NAYA METHOD ADD KARO - Wilbur ke liye
    public float getWilburBalance() {
        UserRecord wilbur = userRecordRepository.findByName("wilbur").orElse(null);
        return wilbur != null ? wilbur.getBalance() : 0.0f;
    }
}