package com.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Order {
    @JsonProperty("orderId")
    private String orderId;
    
    @JsonProperty("customerId")
    private String customerId;
    
    @JsonProperty("amount")
    private double amount;
    
    @JsonProperty("category")
    private String category;
    
    @JsonProperty("discount")
    private double discount;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("approved")
    private boolean approved;

    // Default constructor
    public Order() {}

    // Constructor
    public Order(String orderId, String customerId, double amount, String category) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.category = category;
        this.discount = 0.0;
        this.status = "PENDING";
        this.approved = false;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", discount=" + discount +
                ", status='" + status + '\'' +
                ", approved=" + approved +
                '}';
    }
}