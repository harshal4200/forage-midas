package com.jpmc.midascore.foundation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    private long userId;  // YEH NAYA FIELD ADD KARO
    private float amount;

    public Balance() {
    }

    // YEH NAYA CONSTRUCTOR - Task ke liye required
    public Balance(long userId, float amount) {
        this.userId = userId;
        this.amount = amount;
    }

    // YEH PURANA CONSTRUCTOR
    public Balance(float amount) {
        this.amount = amount;
    }

    // GETTERS AND SETTERS
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    // IMPORTANT: toString() METHOD BILKUL CHANGE MAT KARO
    @Override
    public String toString() {
        return "Balance {amount=" + amount + "}";
    }
}