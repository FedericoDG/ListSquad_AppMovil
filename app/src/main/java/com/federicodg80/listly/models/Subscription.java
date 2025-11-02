package com.federicodg80.listly.models;

import java.time.LocalDateTime;

public class Subscription {
    private int SubscriptionId;
    private String Name;
    private String Description;
    private double Price;
    private LocalDateTime StartDate;
    private LocalDateTime EndDate;
    private boolean IsActive;

    // Constructor
    public Subscription(int subscriptionId, String name, String description, double price, LocalDateTime startDate, LocalDateTime endDate, boolean isActive) {
        SubscriptionId = subscriptionId;
        Name = name;
        Description = description;
        Price = price;
        StartDate = startDate;
        EndDate = endDate;
        IsActive = isActive;
    }

    // Getters
    public int getSubscriptionId() {
        return SubscriptionId;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public double getPrice() {
        return Price;
    }

    public LocalDateTime getStartDate() {
        return StartDate;
    }

    public LocalDateTime getEndDate() {
        return EndDate;
    }

    public boolean isActive() {
        return IsActive;
    }
}
