package com.onymos.stocktrading.service;

import com.onymos.stocktrading.model.Order;
import com.onymos.stocktrading.model.OrderType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class OrderBookService {
    private static final Logger logger = LoggerFactory.getLogger(OrderBookService.class);
    private static final int MAX_TICKERS = 1024;
    
    // Arrays of AtomicReference to create lock-free linked lists for each ticker.
    private final AtomicReference<Order>[] buyOrders;
    private final AtomicReference<Order>[] sellOrders;
    private final AtomicInteger orderIdGenerator = new AtomicInteger(0);
    
    @SuppressWarnings("unchecked")
    public OrderBookService() {
        buyOrders = new AtomicReference[MAX_TICKERS];
        sellOrders = new AtomicReference[MAX_TICKERS];
        for (int i = 0; i < MAX_TICKERS; i++) {
            buyOrders[i] = new AtomicReference<>(null);
            sellOrders[i] = new AtomicReference<>(null);
        }
    }
    
    // Maps a ticker string to an index in the array.
    private int getTickerIndex(String ticker) {
        return Math.abs(ticker.hashCode()) % MAX_TICKERS;
    }
    
    /**
     * Adds an order to the appropriate order book using a lock-free CAS loop.
     */
    public int addOrder(OrderType type, String ticker, int quantity, double price) {
        if (quantity <= 0 || price <= 0) {
            throw new IllegalArgumentException("Quantity and price must be positive");
        }
        int orderId = orderIdGenerator.incrementAndGet();
        Order newOrder = new Order(type, ticker, quantity, price, orderId);
        int index = getTickerIndex(ticker);
        AtomicReference<Order> head = (type == OrderType.BUY) ? buyOrders[index] : sellOrders[index];
        
        while (true) {
            Order currentHead = head.get();
            newOrder.getNext().set(currentHead);
            if (head.compareAndSet(currentHead, newOrder)) {
                logger.info("Added: {}", newOrder);
                break;
            }
        }
        return orderId;
    }
    
    /**
     * Matches orders for all tickers.
     */
    public void matchOrders() {
        for (int i = 0; i < MAX_TICKERS; i++) {
            matchOrdersForTicker(i);
        }
    }
    
    /**
     * Matches orders for a specific ticker (O(n) time for n orders in that ticker's order book).
     */
    private void matchOrdersForTicker(int index) {
        AtomicReference<Order> buyHead = buyOrders[index];
        AtomicReference<Order> sellHead = sellOrders[index];
        
        // Find the highest buy order.
        Order highestBuy = null;
        Order current = buyHead.get();
        while (current != null) {
            if (highestBuy == null || current.getPrice() > highestBuy.getPrice()) {
                highestBuy = current;
            }
            current = current.getNext().get();
        }
        
        // Find the lowest sell order.
        Order lowestSell = null;
        current = sellHead.get();
        while (current != null) {
            if (lowestSell == null || current.getPrice() < lowestSell.getPrice()) {
                lowestSell = current;
            }
            current = current.getNext().get();
        }
        
        // If a match is possible (buy price >= sell price), process the match.
        if (highestBuy != null && lowestSell != null && highestBuy.getPrice() >= lowestSell.getPrice()) {
            int matchedQuantity = Math.min(highestBuy.getQuantity(), lowestSell.getQuantity());
            highestBuy.setQuantity(highestBuy.getQuantity() - matchedQuantity);
            lowestSell.setQuantity(lowestSell.getQuantity() - matchedQuantity);
            logger.info("MATCH: {} with {} for {} units at ${}",
                    highestBuy, lowestSell, matchedQuantity, lowestSell.getPrice());
            
            // Remove orders that are fully matched.
            if (highestBuy.getQuantity() <= 0) {
                removeOrder(buyHead, highestBuy);
            }
            if (lowestSell.getQuantity() <= 0) {
                removeOrder(sellHead, lowestSell);
            }
        }
    }
    
    /**
     * Removes an order from the linked list using a lock-free CAS loop.
     */
    private void removeOrder(AtomicReference<Order> head, Order orderToRemove) {
        Order current = head.get();
        // If the order to remove is at the head.
        if (current != null && current.getId() == orderToRemove.getId()) {
            head.compareAndSet(current, current.getNext().get());
            logger.info("Removed: {}", orderToRemove);
            return;
        }
        // Otherwise, traverse the list.
        while (current != null) {
            Order next = current.getNext().get();
            if (next != null && next.getId() == orderToRemove.getId()) {
                if (current.getNext().compareAndSet(next, next.getNext().get())) {
                    logger.info("Removed: {}", orderToRemove);
                    return;
                }
            }
            current = current.getNext().get();
        }
    }
    
    // Optional: Returns a snapshot of the current order book for debugging.
    public String getOrderBookSnapshot() {
        // ANSI escape codes for colors:
        final String GREEN = "\u001B[32m";  // For BUY orders
        final String RED = "\u001B[31m";    // For SELL orders
        final String RESET = "\u001B[0m";   // Reset to default

        StringBuilder sb = new StringBuilder("===== ORDER BOOK SNAPSHOT =====\n");
        for (int i = 0; i < MAX_TICKERS; i++) {
            Order buyOrder = buyOrders[i].get();
            Order sellOrder = sellOrders[i].get();
            if (buyOrder != null || sellOrder != null) {
                sb.append("Ticker Index: ").append(i).append("\n");
                sb.append("  BUY Orders:\n");
                while (buyOrder != null) {
                    // Append the BUY order in green
                    sb.append("    ").append(GREEN).append(buyOrder).append(RESET).append("\n");
                    buyOrder = buyOrder.getNext().get();
                }
                sb.append("  SELL Orders:\n");
                while (sellOrder != null) {
                    // Append the SELL order in red
                    sb.append("    ").append(RED).append(sellOrder).append(RESET).append("\n");
                    sellOrder = sellOrder.getNext().get();
                }
            }
        }
        sb.append("================================\n");
        return sb.toString();
    }
}

