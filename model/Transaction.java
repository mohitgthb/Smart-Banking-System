package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {

    private String type;
    private double amount;
    private LocalDateTime date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getDate(){
        return date;
    }

    @Override
    public String toString() {
        return type + ":" + amount + ":" + date;
    }
}
