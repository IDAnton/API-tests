package ru.ivanov.queues;

public record Message(String orderId, String status) {
    public String getOrderId() {
        return orderId;
    }

    public String getStatus() {
        return status;
    }
}
