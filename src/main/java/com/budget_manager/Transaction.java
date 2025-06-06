package com.budget_manager;

import java.sql.Date;
import java.time.LocalDate;

public class Transaction {

    private int id;
    private double amount;
    private String type;
    private String category;
    private String description;
    private LocalDate date;
    private int userId;

    Transaction(int id, double amount, String type, String category, String description, LocalDate date, int userId) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.date = date;
        this.userId = userId;
    }
    Transaction(double amount, String type, String category, String description, LocalDate date, int userId) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.description = description;
        this.date = date;
        this.userId = userId;
    }

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }
    public double getAmount() {
        return amount;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Transaction id: " + id +", Amount: " + amount + ", Type: " + type + ", Category: " + category + ", Description: " + description + ", Date: " + date
                + ", User ID: " + userId;

    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
