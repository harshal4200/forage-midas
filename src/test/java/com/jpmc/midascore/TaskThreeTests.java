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
public class TaskThreeTests {
    static final Logger logger = LoggerFactory.getLogger(TaskThreeTests.class);

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private UserPopulator userPopulator;

    @Autowired
    private FileLoader fileLoader;

    @Autowired
    private UserRecordRepository userRecordRepository; // YEH ADD KARO

    @Test
    void task_three_verifier() throws InterruptedException {
        userPopulator.populate();
        String[] transactionLines = fileLoader.loadStrings("/test_data/mnbvcxz.vbnm");

        for (String transactionLine : transactionLines) {
            Transaction transaction = parseTransactionLine(transactionLine);
            kafkaProducer.send("midas-topic", transaction);
        }

        Thread.sleep(5000); // Transactions process hone ka time do

        // Direct answer print karo
        UserRecord waldorf = userRecordRepository.findByName("waldorf").orElse(null);
        if (waldorf != null) {
            float balance = waldorf.getBalance();
            int roundedBalance = (int) Math.floor(balance);
            logger.info("🎯🎯🎯 WALDORF FINAL BALANCE: " + roundedBalance + " 🎯🎯🎯");
            logger.info("SUBMIT THIS NUMBER: " + roundedBalance);
        } else {
            logger.info("Waldorf user not found!");
        }

        logger.info("----------------------------------------------------------");
        logger.info("TEST COMPLETED - Check above for Waldorf's balance");
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