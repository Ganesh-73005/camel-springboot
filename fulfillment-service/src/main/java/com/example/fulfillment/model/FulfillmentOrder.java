package com.example.fulfillment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FulfillmentOrder {
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
    
    @JsonProperty("shippingMethod")
    private String shippingMethod;
    
    @JsonProperty("priority")
    private int priority;
    
    @JsonProperty("fulfillmentStatus")
    private String fulfillmentStatus;
    
    @JsonProperty("approved")
    private Boolean approved;

// Add getter and setter
    public Boolean isApproved() {
    return approved;
}

    public void setApproved(Boolean approved) {
    this.approved = approved;
}
    // Default constructor
    public FulfillmentOrder() {
        this.fulfillmentStatus = "PENDING";
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

    public String getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFulfillmentStatus() {
        return fulfillmentStatus;
    }

    public void setFulfillmentStatus(String fulfillmentStatus) {
        this.fulfillmentStatus = fulfillmentStatus;
    }

    @Override
    public String toString() {
        return "FulfillmentOrder{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", discount=" + discount +
                ", status='" + status + '\'' +
                ", shippingMethod='" + shippingMethod + '\'' +
                ", priority=" + priority +
                ", fulfillmentStatus='" + fulfillmentStatus + '\'' +
                '}';
    }
}