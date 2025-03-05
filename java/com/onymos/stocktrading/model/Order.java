package com.onymos.stocktrading.model;

import java.util.concurrent.atomic.AtomicReference;

public class Order {
    private final OrderType type;
    private final String ticker;
    private int quantity;
    private final double price;
    private final int id;
    private final long timestamp;
    private final AtomicReference<Order> next;

    public Order(OrderType type, String ticker, int quantity, double price, int id) {
        this.type = type;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.id = id;
        this.timestamp = System.nanoTime();
        this.next = new AtomicReference<>(null);
    }

    public OrderType getType() {
        return type;
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public AtomicReference<Order> getNext() {
        return next;
    }

    @Override
    public String toString() {
        return String.format("Order[%d]: %s %s Q:%d P:$%.2f", id, type, ticker, quantity, price);
    }
}
