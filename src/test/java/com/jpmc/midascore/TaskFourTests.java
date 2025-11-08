package com.jpmc.midascore;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRecordRepository;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class TaskFourTests {
    static final Logger logger = LoggerFactory.getLogger(TaskFourTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRecordRepository userRecordRepository; // YEH ADD KARO

    @Test
    void task_four_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/alskdjfh.fhdjsk");

        for (String transactionLine : transactionLines) {
            Transaction transaction = parseTransactionLine(transactionLine);
            kafkaProducer.send("midas-topic", transaction);
        }

        Thread.sleep(5000); // Transactions + incentive API calls ke liye extra time

        // YEH CODE ADD KARO - Direct wilbur ka balance print karo
        UserRecord wilbur = userRecordRepository.findByName("wilbur").orElse(null);
        if (wilbur != null) {
            float balance = wilbur.getBalance();
            int roundedBalance = (int) Math.floor(balance);
            logger.info("🎯🎯🎯 WILBUR FINAL BALANCE: " + roundedBalance + " 🎯🎯🎯");
            logger.info("SUBMIT THIS NUMBER: " + roundedBalance);
        } else {
            logger.info("Wilbur user not found!");
        }

        logger.info("----------------------------------------------------------");
        logger.info("TEST COMPLETED - Check above for Wilbur's balance");

        // Infinite loop comment karo
        // while (true) {
        //     Thread.sleep(20000);
        //     logger.info("...");
        // }
    }

    private Transaction parseTransactionLine(String transactionLine) {
        String[] parts = transactionLine.split(",");
        if (parts.length == 3) {
            long senderId = Long.parseLong(parts[0].trim());
            long recipientId = Long.parseLong(parts[1].trim());
            float amount = Float.parseFloat(parts[2].trim());
            return new Transaction(senderId, recipientId, amount);
        } else {
            throw new IllegalArgumentException("Invalid transaction line: " + transactionLine);
        }
    }
}