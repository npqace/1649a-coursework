package models.enums;

public enum OrderStatus {
    PENDING, // initiat when create an order
    CONFIRMED, // when the order is confirmed (book is available)
    SHIPPING, // when the order is being shipped
    DELIVERED, // when the order is delivered / completed
    CANCELLED // when the order is cancelled
}
