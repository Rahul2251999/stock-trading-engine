package com.onymos.stocktrading.controller;

import com.onymos.stocktrading.model.OrderType;
import com.onymos.stocktrading.service.OrderBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderBookService orderBookService;

    @Autowired
    public OrderController(OrderBookService orderBookService) {
        this.orderBookService = orderBookService;
    }

    // Endpoint to add an order.
    // Example URL: POST /api/orders/add?type=BUY&ticker=AAPL&quantity=10&price=150.0
    @PostMapping("/add")
    public int addOrder(@RequestParam("type") OrderType type,
                        @RequestParam("ticker") String ticker,
                        @RequestParam("quantity") int quantity,
                        @RequestParam("price") double price) {
        return orderBookService.addOrder(type, ticker, quantity, price);
    }

    // Endpoint to manually trigger matching orders.
    @GetMapping("/match")
    public void matchOrders() {
        orderBookService.matchOrders();
    }

    // Endpoint to retrieve a snapshot of the order book.
    @GetMapping("/snapshot")
    public String getOrderBookSnapshot() {
        return orderBookService.getOrderBookSnapshot();
    }
}
