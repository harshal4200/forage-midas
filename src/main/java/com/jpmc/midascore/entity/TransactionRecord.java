package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_record")
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    private float amount;

    private LocalDateTime timestamp;

    // Constructors
    public TransactionRecord() {
        this.timestamp = LocalDateTime.now();
    }

    private float incentive; // YEH ADD KARO

    // Constructor update karo
    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentive) {
        this();
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.incentive = incentive;
    }

    // GetterSetter add karo
    public float getIncentive() { return incentive; }
    public void setIncentive(float incentive) { this.incentive = incentive; }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UserRecord getSender() { return sender; }
    public void setSender(UserRecord sender) { this.sender = sender; }

    public UserRecord getRecipient() { return recipient; }
    public void setRecipient(UserRecord recipient) { this.recipient = recipient; }

    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}