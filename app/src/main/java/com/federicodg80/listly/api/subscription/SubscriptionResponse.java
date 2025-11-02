package com.federicodg80.listly.api.subscription;

public class SubscriptionResponse {
    public int SubscriptionId;
    public String Name;
    public String Description;
    public double Price;
    public String MercadoPagoPreferenceId;
    public String InitPoint;
    public String SandboxInitPoint;

    public SubscriptionResponse(int subscriptionId, String name, String description, double price, String mercadoPagoPreferenceId, String initPoint, String sandboxInitPoint) {
        SubscriptionId = subscriptionId;
        Name = name;
        Description = description;
        Price = price;
        MercadoPagoPreferenceId = mercadoPagoPreferenceId;
        InitPoint = initPoint;
        SandboxInitPoint = sandboxInitPoint;
    }

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

    public String getMercadoPagoPreferenceId() {
        return MercadoPagoPreferenceId;
    }

    public String getInitPoint() {
        return InitPoint;
    }

    public String getSandboxInitPoint() {
        return SandboxInitPoint;
    }
}
